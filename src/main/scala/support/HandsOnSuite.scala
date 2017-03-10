package support

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.exceptions.TestFailedException
import org.scalatest.matchers.Matcher
import org.scalatest.time.{Millis, Seconds, Span}

import scala.language.experimental.macros

trait HandsOnSuite extends FunSpec with Matchers with ScalaFutures {
  implicit val suite: HandsOnSuite = this
  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  def __ : Matcher[Any] = throw new NotImplementedError("__")
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
  def runTest(testName: String, suite: HandsOnSuite)(testFun: => Unit)(context: TestContext) {
    suite.it(testName) {
      val startLine = context.testStartLine
      val endLine = context.testEndLine
      lazy val testSourceFile: Array[(String, Int)] = context.source.zipWithIndex.map(t => (t._1, t._2 + 1))

      def testCtx(errorLine: Int): String =
        prettyShow(testSourceFile.slice(startLine - 1, endLine + 1), errorLine).mkString("\n")

      def errorCtx(errorLine: Int): String =
        prettyShow(testSourceFile.slice(errorLine - 2, errorLine + 1), errorLine).mkString("\n")

      def ctx(errorLines: List[Int]): Option[String] = {
        val (inTest, outTest) = errorLines.partition(line => startLine <= line && line <= endLine)
        val errorCtxs = outTest.sorted.map(errorCtx)
        val split = if (errorCtxs.isEmpty) "" else "\n...\n"
        if (inTest.nonEmpty)
          Some((errorCtxs ::: (split :: testCtx(inTest.min) :: Nil)).mkString("\n"))
        else
          Some((errorCtxs ::: (split :: Nil)).mkString("\n"))
      }

      def exceptionMessage(t: Throwable): String =
        Option(t.getMessage).getOrElse("")

      def exceptionToLocation(st: StackTraceElement): String =
        "src/test/scala/" + suitePackage + java.io.File.separator + st.getFileName + ":" + st.getLineNumber

      def suitePackage =
        suite.getClass.getPackage.getName

      try {
        testFun
      } catch {
        case e: TestFailedException =>
          val location = e.failedCodeFileNameAndLineNumberString.map(suitePackage + java.io.File.separator + _)
          throw new MyTestFailedException(exceptionMessage(e), ctx(e.failedCodeLineNumber.toList), e, location)
        case e: NotImplementedError =>
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
        case e: Throwable =>
          val firstGoodStackTrace: Option[StackTraceElement] = e.getStackTrace.find(_.getClassName.contains(suite.getClass.getName))
          val location = firstGoodStackTrace.map(exceptionToLocation)
          val failContext = firstGoodStackTrace.map(_.getLineNumber).map(lineNum => ctx(List(lineNum)))
          val myctx = failContext.getOrElse("") + e.getStackTrace.take(7).mkString("\n")
          val mes = e.toString + location.map("\n    at " + _)
          throw new MyException(mes, Some(myctx), e, location)
      }
    }
  }

  private def prettyShow(source: Array[(String, Int)], errorLine: Int): List[String] = {
    source.toList.map { case (line, number) =>
      val prefix: String = if (number == errorLine) " ->" else "   "
      prefix + leftPad(number, 4) + " |" + line
    }
  }

  private def leftPad(number: Int, size: Int, char: String = " "): String =
    (char * (size - number.toString.length)) + number.toString
}
