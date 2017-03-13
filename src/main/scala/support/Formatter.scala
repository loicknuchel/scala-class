package support

object Formatter {

  import Utils._

  val missingValue = "You have to replace __ by correct value"
  val missingImplementation = "You have to replace ??? by correct implementation"

  private val pendingHeader = "\n*******************************************\n               TEST PENDING                \n*******************************************\n"
  private val pendingFooter = "\n*******************************************\n"
  private val failureHeader = "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n               TEST FAILED                 \n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"
  private val failureFooter = "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"

  def formatInfo(suiteName: String, testName: String, errOpt: Option[MyException], pending: Boolean): String = {
    def formatHeader(pending: Boolean): String =
      if (pending) pendingHeader else failureHeader

    def locationLine(name: String, content: String): String =
      padRight(name, 9) + ": " + content.replace("\n", "") + "\n"

    def formatLocation(suiteName: String, testName: String, fileName: Option[String]): String =
      locationLine("Suite", suiteName) +
        locationLine("Test", testName) +
        fileName.map(f => locationLine("File", f)).getOrElse("")

    def formatMessage(message: Option[String]): String =
      message.map(m => "\n" + m).getOrElse("")

    def formatCode(ctx: TestContext, errors: Option[List[Int]]): String =
      errors.map("\n" + codeSection(ctx, _) + "\n").getOrElse("")

    def formatStackTrace(err: MyException): String = err match {
      case _: MyTestPendingException => ""
      case _: MyNotImplementedException => ""
      case _: MyTestFailedException => ""
      case e => "\n" + e.getCause.getStackTrace.take(7).mkString("\n") + "\n"
    }

    def formatFooter(pending: Boolean): String =
      if (pending) pendingFooter else failureFooter

    val sb = new StringBuilder()
    sb.append(formatHeader(pending))
    sb.append(formatLocation(suiteName, testName, errOpt.flatMap(_.fileName)))
    errOpt.foreach(err => {
      sb.append(formatMessage(Option(err.getMessage)))
      sb.append(formatCode(err.ctx, err.errors))
      sb.append(formatStackTrace(err))
    })
    sb.append(formatFooter(pending))
    sb.toString
  }

  private def codeSection(ctx: TestContext, errors: List[Int]): String = {
    def formatLine(lineNumber: Int, line: String, hasError: Boolean): String =
      (if (hasError) " ->" else "   ") + padLeft(lineNumber.toString, 4) + " |" + line

    def formatBlock(lines: List[(Int, String)], error: Int): List[String] =
      lines.map { case (lineNumber, codeLine) => formatLine(lineNumber, codeLine, lineNumber == error) }

    def formatTest(context: TestContext, error: Int): String =
      formatBlock(context.lines.slice(context.startLine - 1, context.endLine + 1), error).mkString("\n")

    def formatError(context: TestContext, error: Int): String =
      formatBlock(context.lines.slice(error - 2, error + 1), error).mkString("\n")

    val (inTest, outTest) = errors.partition(line => ctx.startLine <= line && line <= ctx.endLine)
    val formattedErrors = outTest.sorted.map(i => formatError(ctx, i))
    val split = if (formattedErrors.isEmpty) "" else "\n...\n"
    if (inTest.nonEmpty)
      (formattedErrors ::: (split :: formatTest(ctx, inTest.min) :: Nil)).mkString("\n")
    else
      (formattedErrors ::: (split :: Nil)).mkString("\n")
  }

  private object Utils {
    def padLeft(text: String, size: Int, char: String = " "): String =
      (char * (size - text.length)) + text

    def padRight(text: String, size: Int, char: String = " "): String =
      text + (char * (size - text.length))
  }

}
