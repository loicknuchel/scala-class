package support

import org.scalatest.exceptions.{TestFailedException, TestPendingException}

class MyException(val fileName: Option[String], val ctx: TestContext, val errors: Option[List[Int]], val message: String, val cause: Throwable) extends Exception(message, cause) {}

class MyTestPendingException(fileName: Option[String], ctx: TestContext, errors: Option[List[Int]], message: String, cause: Throwable) extends MyException(fileName, ctx, errors, message, cause) {}

class MyNotImplementedException(fileName: Option[String], ctx: TestContext, errors: Option[List[Int]], message: String, cause: Throwable) extends MyException(fileName, ctx, errors, message, cause) {}

class MyTestFailedException(fileName: Option[String], ctx: TestContext, errors: Option[List[Int]], message: String, cause: Throwable) extends MyException(fileName, ctx, errors, message, cause) {}

object MyException {
  def pending(suite: HandsOnSuite, ctx: TestContext, e: TestPendingException): MyTestPendingException = {
    val stack = e.getStackTrace()(2)
    val location = getLocation(suite, stack)
    val errors = List(stack.getLineNumber)
    new MyTestPendingException(Some(location), ctx, Some(errors), Formatter.missingValue, e)
  }

  def notImplemented(suite: HandsOnSuite, ctx: TestContext, e: NotImplementedError): MyNotImplementedException = {
    val stacks = e.getStackTrace.take(7).filter(isSuiteClass(suite, _)).toList
    val locationOpt = stacks.headOption.map(getLocation(suite, _))
    val errors = stacks.map(_.getLineNumber).distinct
    new MyNotImplementedException(locationOpt, ctx, nonEmpty(errors), Formatter.missingImplementation, e)
  }

  def failed(suite: HandsOnSuite, ctx: TestContext, e: TestFailedException): MyTestFailedException = {
    val locationOpt = e.failedCodeFileNameAndLineNumberString.map(getPackage(suite, _))
    val errors = e.failedCodeLineNumber.toList
    new MyTestFailedException(locationOpt, ctx, Some(errors), Option(e.getMessage).getOrElse(""), e)
  }

  def unknown(suite: HandsOnSuite, ctx: TestContext, e: Throwable): MyException = {
    val stacks = e.getStackTrace.filter(isSuiteClass(suite, _)).toList
    val locationOpt = stacks.headOption.map(getLocation(suite, _))
    val errors = stacks.map(_.getLineNumber).distinct
    new MyException(locationOpt, ctx, nonEmpty(errors), e.toString, e)
  }

  private def getPackage(suite: HandsOnSuite, name: String): String =
    List("src", "test", "scala", suite.getClass.getPackage.getName, name).mkString(java.io.File.separator)

  private def getLocation(suite: HandsOnSuite, st: StackTraceElement): String =
    getPackage(suite, st.getFileName + ":" + st.getLineNumber)

  private def isSuiteClass(suite: HandsOnSuite, e: StackTraceElement): Boolean =
    e.getClassName.contains(suite.getClass.getName)

  private def nonEmpty[T](l: List[T]): Option[List[T]] =
    if (l.nonEmpty) Some(l) else None
}
