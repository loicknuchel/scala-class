package support

object Formatter {

  import Utils._

  val missingValue = "You have to replace __ by correct value"
  val missingImplementation = "You have to replace ??? by correct implementation"

  private val pendingHeader = "\n*******************************************\n               TEST PENDING                \n*******************************************\n"
  private val pendingFooter = "\n*******************************************\n"
  private val failureHeader = "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n               TEST FAILED                 \n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"
  private val failureFooter = "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"

  def formatMessage(suiteName: String, testName: String, err: Option[MyException], pending: Boolean): String = {
    def formatHeader(pending: Boolean): String = if (pending) pendingHeader else failureHeader

    def locationLine(name: String, content: String): String = padRight(name, 9) + ": " + content.replace("\n", "") + "\n"

    def formatLocation(suiteName: String, testName: String, fileName: Option[String]): String =
      locationLine("Suite", suiteName) +
        locationLine("Test", testName) +
        fileName.map(f => locationLine("File", f)).getOrElse("")

    def formatMessage(message: Option[String]): String = message.map(m => "\n" + m).getOrElse("")

    def formatContext(context: Option[String]): String = context.map(c => "\n" + c + "\n").getOrElse("")

    def formatStackTrace(err: Option[MyException]): String = err.map {
      case _: MyTestPendingException => ""
      case _: MyNotImplException => ""
      case _: MyTestFailedException => ""
      case e => "\n" + e.getCause.getStackTrace.take(7).mkString("\n") + "\n"
    }.getOrElse("")

    def formatFooter(pending: Boolean): String = if (pending) pendingFooter else failureFooter

    val sb = new StringBuilder()
    sb.append(formatHeader(pending))
    sb.append(formatLocation(suiteName, testName, err.flatMap(_.fileNameAndLineNumber)))
    sb.append(formatMessage(err.map(_.getMessage)))
    sb.append(formatContext(err.flatMap(_.context)))
    sb.append(formatStackTrace(err))
    sb.append(formatFooter(pending))
    sb.toString
  }

  def formatCode(ctx: TestContext, errorLines: List[Int]): String = {
    def testCtx(context: TestContext, errorLine: Int): String =
      prettyShow(context.lines.slice(context.startLine - 1, context.endLine + 1), errorLine).mkString("\n")

    def errorCtx(context: TestContext, errorLine: Int): String =
      prettyShow(context.lines.slice(errorLine - 2, errorLine + 1), errorLine).mkString("\n")

    def prettyShow(source: List[(Int, String)], errorLine: Int): List[String] =
      source.map { case (lineNumber, codeLine) =>
        formatCodeLine(lineNumber == errorLine, lineNumber, codeLine)
      }

    def formatCodeLine(isError: Boolean, lineNumber: Int, codeLine: String): String =
      (if (isError) " ->" else "   ") + padLeft(lineNumber.toString, 4) + " |" + codeLine

    val (inTest, outTest) = errorLines.partition(line => ctx.startLine <= line && line <= ctx.endLine)
    val errorCtxs = outTest.sorted.map(i => errorCtx(ctx, i))
    val split = if (errorCtxs.isEmpty) "" else "\n...\n"
    if (inTest.nonEmpty)
      (errorCtxs ::: (split :: testCtx(ctx, inTest.min) :: Nil)).mkString("\n")
    else
      (errorCtxs ::: (split :: Nil)).mkString("\n")
  }


  private object Utils {
    def padLeft(text: String, size: Int, char: String = " "): String =
      (char * (size - text.length)) + text

    def padRight(text: String, size: Int, char: String = " "): String =
      text + (char * (size - text.length))
  }

}
