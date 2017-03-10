package support

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.exceptions.TestFailedException
import org.scalatest.matchers.Matcher
import org.scalatest.time.{Millis, Seconds, Span}

import scala.language.experimental.macros

trait HandsOnSuite extends FunSpec with Matchers with ScalaFutures {
  def __ : Matcher[Any] = {
    throw new NotImplementedError("__")
  }

  implicit val suite: HandsOnSuite = this
  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  def exercice(testName: String)(testFun: Unit)(implicit suite: HandsOnSuite): Unit = macro HandsOnMacro.apply

  protected override def runTest(testName: String, args: Args) = {
    if (!CustomStopper.oneTestFailed) {
      super.runTest(testName, args.copy(reporter = new CustomReporter(args.reporter), stopper = CustomStopper))
    } else {
      SucceededStatus
    }
  }
}

object HandsOnSuite {
  def testBody(testName: String, suite: HandsOnSuite)(testFun: => Unit)(context: TestContext) {
    suite.it(testName) {
      val testExpressionLineStart = context.testStartLine
      val testExpressionLineEnd = context.testEndLine
      lazy val testSourceFile: Array[(String, Int)] = sourceProcessor(context.source)

      def testCtx(errorLine: Int): String = {
        prettyShow(testSourceFile.drop(testExpressionLineStart - 1).take(testExpressionLineEnd - testExpressionLineStart + 2), errorLine).mkString("\n")
      }

      def errorCtx(errorLine: Int): String = {
        prettyShow(testSourceFile.drop(errorLine - 2).take(3), errorLine).mkString("\n")
      }

      def ctx(errorLines: List[Int]): Option[String] = {
        def isInTest(line: Int): Boolean = (line >= testExpressionLineStart && line <= testExpressionLineEnd)

        val (inTest, outTest) = errorLines.partition(isInTest)
        val errorCtxs = outTest.sorted.map(errorCtx)
        val split = if (errorCtxs.isEmpty) "" else "\n...\n"
        if (inTest.size > 0)
          Some((errorCtxs ::: (split :: testCtx(inTest.min) :: Nil)).mkString("\n"))
        else
          Some((errorCtxs ::: (split :: Nil)).mkString("\n"))
      }

      def exceptionMessage(t: Throwable): String = Option(t.getMessage).getOrElse("")

      def exceptionToLocation(st: StackTraceElement): String = {
        "src/test/scala/" + suitePackage + java.io.File.separator + st.getFileName + ":" + st.getLineNumber
      }

      def suitePackage = {
        suite.getClass.getPackage.getName // getPackage.toString
      }

      try {
        testFun
      } catch {
        case e: TestFailedException => {
          val location = e.failedCodeFileNameAndLineNumberString.map(suitePackage + java.io.File.separator + _)
          throw new MyTestFailedException(exceptionMessage(e), ctx(e.failedCodeLineNumber.toList), e, location)
        }
        case e: NotImplementedError => {
          val mes = exceptionMessage(e)
          mes match {
            case "__" =>
              val notimpl = e.getStackTrace()(2)
              val location = exceptionToLocation(notimpl)
              throw new MyTestPendingException(mes, ctx(List(notimpl.getLineNumber)), e, Some(location))
            case _ =>
              val notimpl = e.getStackTrace()(1)
              val secondLineNumber = e.getStackTrace()(2).getLineNumber
              val location = exceptionToLocation(notimpl)
              throw new MyNotImplException(mes, ctx(List(notimpl.getLineNumber, secondLineNumber)), e, Some(location))
          }
        }
        case e: Throwable => {
          val firstGoodStackTrace: Option[StackTraceElement] = e.getStackTrace.find(_.getClassName.contains(suite.getClass.getName))
          val location = firstGoodStackTrace.map(exceptionToLocation)
          val failContext = firstGoodStackTrace.map(_.getLineNumber).map(lineNum => ctx(List(lineNum)))
          val myctx = failContext.getOrElse("") + e.getStackTrace.take(7).mkString("\n")
          val mes = e.toString + location.map("\n    at " + _)
          throw new MyException(mes, Some(myctx), e, location)
        }
      }
    }
  }

  private def prettyShow(source: Array[(String, Int)], errorLine: Int): List[String] = {
    def intLen(i: Int) = i.toString.length

    val len: Int = 4

    def completewithspace(i: Int): String = {
      (" " * (len - intLen(i))) + i.toString
    }

    def spacehead(s: String): String = {
      val space = "(\\s*).*".r
      s match {
        case space(spaces) => spaces
        case _ => ""
      }
    }

    source.toList.map { case (line, number) =>
      val prefix: String = if (number == errorLine) " ->" else "   "
      prefix + completewithspace(number) + " |" + line
    }
  }

  private def sourceProcessor(source: Array[String]): Array[(String, Int)] = {
    source.zipWithIndex.map(t => (t._1, t._2 + 1))
  }
}
