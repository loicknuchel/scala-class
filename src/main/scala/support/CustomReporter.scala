package support

import org.scalatest.Reporter
import org.scalatest.events._

class CustomReporter(other: Reporter) extends Reporter {
  var failed = false

  def headerFail = "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n               TEST FAILED                 \n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
  def footerFail = "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
  def headerPending = "\n*******************************************\n               TEST PENDING                \n*******************************************"
  def footerPending = "*******************************************"

  def sendInfo(evt: Event, header: String, suite: String, test: String, location: Option[String], message: Option[String], context: Option[String], footer: String) {
    var i = 0
    var toto = evt.ordinal.nextNewOldPair._2

    def mess(s: String) = {
      other(InfoProvided(evt.ordinal, s, None, None, Some(IndentedText(s, s, 0)), evt.location, evt.payload, evt.threadName, evt.timeStamp))
      toto = toto.next
    }

    val sb = new StringBuilder()
    header.split("\n").foreach(sb.append(_).append("\n"))

    sb.append("Suite    : " + suite.replace("\n", "")).append("\n")
      .append("Test     : " + test.replace("\n", "")).append("\n")

    location.collect({ case f =>
      sb.append("File     : " + f.replace("\n", "")).append("\n")
    })
    message.collect({ case m =>
      sb.append("").append("\n")
      m.split("\n").foreach(sb.append(_).append("\n"))
    })
    context.collect({ case c =>
      sb.append("").append("\n")
      c.split("\n").foreach(sb.append(_).append("\n"))
    })
    sb.append("").append("\n")
    footer.split("\n").foreach(sb.append(_).append("\n"))


    mess(sb.toString)

    CustomStopper.testFailed
  }

  def sendFail(evt: Event, e: MyException, suite: String, test: String) = {
    sendInfo(evt, headerFail
      , suite
      , test
      , e.fileNameAndLineNumber
      , Option(e.getMessage)
      , e.context
      , footerFail
    )
  }

  def sendPending(evt: Event, e: MyException, suite: String, test: String, mess: Option[String]) = {
    sendInfo(evt, headerPending
      , suite
      , test
      , e.fileNameAndLineNumber
      , mess
      , e.context
      , footerPending
    )
  }

  def apply(event: Event) {
    event match {
      case e: TestFailed => {
        e.throwable match {
          //pour les erreurs d'assertions => sans stacktrace
          case Some(failure: MyTestFailedException) =>
            sendFail(event, failure, e.suiteName, e.testName)
          //pour les __ => avec context
          case Some(pending: MyTestPendingException) =>
            sendPending(event, pending, e.suiteName, e.testName, Some("You have to replace __ by correct values"))
          //pour les ??? => sans context
          case Some(pending: MyNotImplException) =>
            sendPending(event, pending, e.suiteName, e.testName, Some("You have to replace ??? by correct implementations"))
          //pour les autres erreurs => avec stacktrace
          case Some(failure: MyException) =>
            sendFail(event, failure, e.suiteName, e.testName)
          //ça ne devrait pas arriver
          case Some(e) =>
            println("something went wrong ("+e.getClass.getCanonicalName+")")
          //ça non plus, un TestFailed a normalement une excepetion attachée
          case None =>
            sendInfo(event, headerFail
              , e.suiteName
              , e.testName
              , None
              , None
              , None
              ,
              footerFail
            )
        }
      }
      case e: TestPending =>
        sendInfo(event, headerPending
          , e.suiteName
          , e.testName
          , None
          , Some("pending")
          , None
          , footerPending)
      case e: InfoProvided =>
        if (e.formatter.isDefined) other(event)
      case _: SuiteCompleted | _: SuiteStarting | _: RunCompleted | _: RunStopped | _: TestStarting | _: TestSucceeded =>
        other(event)
      case _ =>
        println(event)
        other(event)
    }
  }
}
