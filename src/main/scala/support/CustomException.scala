package support

import org.scalatest.exceptions.{TestFailedException, TestPendingException}

class MyException(val fileNameAndLineNumber: Option[String], val context: Option[String], val message: String, val cause: Throwable) extends Exception(message, cause) {}

class MyTestPendingException(fileNameAndLineNumber: Option[String], context: Option[String], message: String, cause: Throwable) extends MyException(fileNameAndLineNumber, context, message, cause) {}

class MyNotImplException(fileNameAndLineNumber: Option[String], context: Option[String], message: String, cause: Throwable) extends MyException(fileNameAndLineNumber, context, message, cause) {}

class MyTestFailedException(fileNameAndLineNumber: Option[String], context: Option[String], message: String, cause: Throwable) extends MyException(fileNameAndLineNumber, context, message, cause) {}

object MyException {
  private def getPackage(suite: HandsOnSuite, name: String): String =
    List("src", "test", "scala", suite.getClass.getPackage.getName, name).mkString(java.io.File.separator)

  private def getLocation(suite: HandsOnSuite, st: StackTraceElement): String =
    getPackage(suite, st.getFileName + ":" + st.getLineNumber)

  def pending(suite: HandsOnSuite, ctx: TestContext, e: TestPendingException): MyTestPendingException = {
    val stack = e.getStackTrace()(2)
    val location = getLocation(suite, stack)
    val formattedCode = Formatter.formatCode(ctx, List(stack.getLineNumber))
    new MyTestPendingException(Some(location), Some(formattedCode), Formatter.missingValue, e)
  }

  def notImpl(suite: HandsOnSuite, ctx: TestContext, e: NotImplementedError): MyNotImplException = {
    val stack = e.getStackTrace()(1)
    val nextStack = e.getStackTrace()(2)
    val location = getLocation(suite, stack)
    val formattedCode = Formatter.formatCode(ctx, List(stack.getLineNumber, nextStack.getLineNumber))
    new MyNotImplException(Some(location), Some(formattedCode), Formatter.missingImplementation, e)
  }

  def failed(suite: HandsOnSuite, ctx: TestContext, e: TestFailedException): MyTestFailedException = {
    val locationOpt = e.failedCodeFileNameAndLineNumberString.map(getPackage(suite, _))
    val formattedCode = Formatter.formatCode(ctx, e.failedCodeLineNumber.toList)
    new MyTestFailedException(locationOpt, Some(formattedCode), Option(e.getMessage).getOrElse(""), e)
  }

  def unknown(suite: HandsOnSuite, ctx: TestContext, e: Throwable): MyException = {
    val firstGoodStack: Option[StackTraceElement] = e.getStackTrace.find(_.getClassName.contains(suite.getClass.getName))
    val locationOpt = firstGoodStack.map(st => getLocation(suite, st))
    val formattedCodeOpt = firstGoodStack.map(st => Formatter.formatCode(ctx, List(st.getLineNumber)))
    new MyException(locationOpt, formattedCodeOpt, e.toString, e)
  }
}
