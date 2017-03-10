package recorder

import org.scalatest.FunSpec
import org.scalatest.exceptions.TestFailedException

trait MyFunSuite extends FunSpec {
  implicit val anchorRecorder = new AnchorRecorder()

  def testPublic(testName: String)(testFun: => Unit) {
    it(testName)(testFun)
  }
}

object MyFunSuite {
  def testBody(testName: String, suite: MyFunSuite, anchorRecorder: AnchorRecorder)(testFun: => Unit)(context: TestContext) {

    suite.testPublic(testName)({
      val testExpressionLineStart = context.testStartLine
      val testExpressionLineEnd = context.testEndLine
      lazy val testSourceFile: Array[(String, Int)] = sourceProcessor(context.source)

      anchorRecorder.reset()

      def testCtx(errorLine: Int): String = {
        MyFunSuite.prettyShow(testSourceFile.drop(testExpressionLineStart - 1).take(testExpressionLineEnd - testExpressionLineStart + 2),
          errorLine, anchorRecorder.records).mkString("\n")
      }

      def errorCtx(errorLine: Int): String = {
        MyFunSuite.prettyShow(testSourceFile.drop(errorLine - 2).take(3), errorLine, anchorRecorder.records).mkString("\n")
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
    })
  }

  def mergeSourceAndAnchor(source: List[(String, Int)], anchorsMessages: List[AnchorValue]): List[(String, Int, Option[String])] = {
    def anchor(line: Int, anchorsMessages: List[AnchorValue]): Option[String] = {
      val mess = anchorsMessages.filter(_.line == line)
        .map(a => a.name + " => " + a.value).mkString("\n")
      if (mess.trim == "") None else Some(mess)
    }

    source match {
      case shead :: stail =>
        (shead._1, shead._2, anchor(shead._2, anchorsMessages)) :: mergeSourceAndAnchor(stail, anchorsMessages)
      case _ => Nil
    }
  }

  def prettyShow(source: Array[(String, Int)], errorLine: Int, anchorsMessages: List[AnchorValue]): List[String] = {
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

    mergeSourceAndAnchor(source.toList, anchorsMessages.reverse).map { case (line, number, oanchor) =>
      val prefix: String = if (number == errorLine) " ->" else "   "
      prefix + completewithspace(number) + " |" + line + oanchor.map(_.split("\n").map(i => "\n         " + spacehead(line) + i).mkString).getOrElse("")
    }
  }

  def sourceProcessor(source: Array[String]): Array[(String, Int)] = {
    source.zipWithIndex.map(t => (t._1, t._2 + 1))
  }
}

