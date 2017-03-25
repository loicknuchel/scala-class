package support

import io.circe.generic.auto._
import org.joda.time.DateTime
import support.Helpers._

import scala.io.Source
import scala.tools.nsc.io.File
import scala.util.{Failure, Success}

case class Log(
                file: String,
                suite: String,
                test: String,
                time: DateTime,
                status: String
              )

object LogRecorder {
  val logFile: String = "logs.json"
  val recording: Boolean = true

  def log(suiteName: String, testName: String, errOpt: Option[MyException]): Unit = {
    if (recording) {
      val log = Log(
        file = errOpt.flatMap(_.fileName).getOrElse("unknown"),
        suite = suiteName,
        test = testName,
        time = new DateTime(),
        status = errOpt match {
          case Some(err: MyTestPendingException) => "pending"
          case Some(err: MyNotImplementedException) => "pending"
          case Some(err: MyTestFailedException) => "error"
          case Some(err: MyException) => "error"
          case Some(err) => "error"
          case _ => "unknown"
        }
      )
      File(logFile).appendAll(asJson(log) + "\n")
    }
  }

  def read(): List[Log] =
    Source.fromFile(logFile).getLines().toList.zipWithIndex.flatMap { case (json, index) =>
      parseJson[Log](json) match {
        case Success(log) => Some(log)
        case Failure(err) => println(s"WARN at line ${index + 1}: " + err.getMessage); None
      }
    }

  def analyse(logs: List[Log]): String = {
    def durations(logs: List[Log]): List[(Log, Long)] =
      logs.sliding(2).map { case before :: after :: Nil =>
        (before, after.time.getMillis - before.time.getMillis)
      }.filter(_._2 < 1000 * 60 * 15).toList

    def duration(logs: List[(Log, Long)]): String =
      (logs.map(_._2).sum / 1000 / 60) + " min"

    def errors(logs: List[(Log, Long)]): String =
      logs.count(_._1.status == "error") + " erreurs"

    durations(logs).groupBy(_._1.suite).map { case (suite, suiteLogs) =>
      s"$suite: ${duration(suiteLogs)}, ${errors(suiteLogs)}\n" +
        suiteLogs.groupBy(_._1.test).map { case (test, testLogs) =>
          s"  $test: ${duration(testLogs)}, ${errors(testLogs)}"
        }.toList.mkString("\n")
    }.toList.sorted.mkString("\n")
  }

  def main(args: Array[String]): Unit = {
    println("Analyse des logs:\n" + analyse(read()))
  }

  private def asJson(log: Log): String =
    s"""{"file":"${log.file}","suite":"${log.suite}","test":"${log.test}","time":"${log.time}","status":"${log.status}"}"""
}
