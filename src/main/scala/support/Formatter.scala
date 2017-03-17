package support

object Formatter {

  val missingValue = "Remplace __ par la valeur attendue"
  val missingImplementation = "Remplace ??? par une implémentation correcte"

  def formatInfo(suiteName: String, testName: String, errOpt: Option[MyException], pending: Boolean): String =
    Info.format(suiteName, testName, errOpt, pending)

  object Info {
    private val width = 60
    private val pendingLine = "*" * width
    private val failureLine = "!" * width
    private val assertFailRegex = "(.*) was not equal to (.*)".r

    def formatHeader(testName: String, pending: Boolean): String = {
      val line = if (pending) pendingLine else failureLine
      val text = if (pending) "EXERCICE" else "ERREUR"
      s"\n$line\n" +
        s"${Utils.padCenter(text + ": " + testName, width)}\n" +
        s"$line\n"
    }

    def formatMessage(message: Option[String]): String =
      message.map {
        // should show 'result' or 'expeced' depending on ??? or __ but I can't know because they were removed :(
        case assertFailRegex(result, expected) => s"\nRésultat incorrect :("
        case m => "\n" + m
      }.getOrElse("")

    def formatCode(ctx: TestContext, errors: Option[List[Int]]): String =
      errors.map("\n" + Code.format(ctx, _) + "\n").getOrElse("")

    def formatStackTrace(err: MyException): String = err match {
      case _: MyTestPendingException => ""
      case _: MyNotImplementedException => ""
      case _: MyTestFailedException => ""
      case e => "\n" + e.getCause.getStackTrace.take(7).mkString("\n") + "\n"
    }

    def formatFooter(fileName: Option[String], pending: Boolean): String =
      "\n" + (if (pending) pendingLine else failureLine) + "\n" +
        fileName.map(_ + "\n").getOrElse("")

    def format(suiteName: String, testName: String, errOpt: Option[MyException], pending: Boolean): String = {
      val sb = new StringBuilder()
      sb.append(formatHeader(testName, pending))
      errOpt.foreach(err => {
        sb.append(formatMessage(Option(err.getMessage)))
        sb.append(formatCode(err.ctx, err.errors))
        sb.append(formatStackTrace(err))
      })
      sb.append(formatFooter(errOpt.flatMap(_.fileName), pending))
      sb.toString
    }
  }

  object Code {
    def formatLine(lineNumber: Int, line: String, hasError: Boolean): String =
      (if (hasError) " ->" else "   ") + Utils.padLeft(lineNumber.toString, 4) + " |" + line

    def formatBlock(lines: List[(Int, String)], error: Int): List[String] =
      lines.map { case (lineNumber, codeLine) => formatLine(lineNumber, codeLine, lineNumber == error) }

    def formatTest(context: TestContext, error: Int): String =
      formatBlock(context.lines.slice(context.startLine - 1, context.endLine + 1), error).mkString("\n")

    def formatError(context: TestContext, error: Int): String =
      "\n" + formatBlock(context.lines.slice(error - 2, error + 1), error).mkString("\n")

    def format(ctx: TestContext, errors: List[Int]): String = {
      val (inTest, outTest) = errors.partition(line => ctx.startLine <= line && line <= ctx.endLine)
      val formattedErrors = outTest.sorted.map(i => formatError(ctx, i))
      val split = if (formattedErrors.isEmpty) "" else "\n...\n"
      if (inTest.nonEmpty)
        (formattedErrors ::: (split :: formatTest(ctx, inTest.min) :: Nil)).mkString("\n")
      else
        (formattedErrors ::: (split :: Nil)).mkString("\n")
    }
  }

  object Utils {
    def padLeft(text: String, size: Int, char: String = " "): String =
      (char * (size - text.length)) + text

    def padRight(text: String, size: Int, char: String = " "): String =
      text + (char * (size - text.length))

    def padCenter(text: String, size: Int, char: String = " "): String =
      (char * ((size - text.length) / 2)) + text
  }

}
