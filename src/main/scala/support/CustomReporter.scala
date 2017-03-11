package support

import org.scalatest.Reporter
import org.scalatest.events._

class CustomReporter(other: Reporter) extends Reporter {
  def apply(event: Event) {
    event match {
      case e: TestFailed =>
        e.throwable match {
          case Some(err: MyTestFailedException) => sendInfo(e, Formatter.formatMessage(e.suiteName, e.testName, Some(err), pending = false))
          case Some(err: MyTestPendingException) => sendInfo(event, Formatter.formatMessage(e.suiteName, e.testName, Some(err), pending = true))
          case Some(err: MyNotImplException) => sendInfo(event, Formatter.formatMessage(e.suiteName, e.testName, Some(err), pending = true))
          case Some(err: MyException) => sendInfo(event, Formatter.formatMessage(e.suiteName, e.testName, Some(err), pending = false))
          case Some(err) => println("something went wrong (" + err.getClass.getCanonicalName + ")")
          case None => sendInfo(event, Formatter.formatMessage(e.suiteName, e.testName, None, pending = false))
        }
      case e: TestPending => sendInfo(event, Formatter.formatMessage(e.suiteName, e.testName, None, pending = true))
      case e: InfoProvided => if (e.formatter.isDefined) other(event)
      case _: SuiteCompleted | _: SuiteStarting | _: RunCompleted | _: RunStopped | _: TestStarting | _: TestSucceeded =>
        other(event)
      case _ =>
        println(event)
        other(event)
    }
  }

  def sendInfo(event: Event, message: String): Unit = {
    other(InfoProvided(event.ordinal, message, None, None, Some(IndentedText(message, message, 0)), event.location, event.payload, event.threadName, event.timeStamp))
    event.ordinal.nextNewOldPair._2.next
    CustomStopper.requestStop()
  }
}
