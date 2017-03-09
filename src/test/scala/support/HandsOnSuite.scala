package support

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.Matcher
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{Args, Matchers, SucceededStatus}
import support.scalatest._

import scala.language.experimental.macros

trait HandsOnSuite extends MyFunSuite with Matchers with ScalaFutures {
  def __ : Matcher[Any] = {
    throw new NotImplementedError("__")
  }

  implicit val suite: MyFunSuite = this
  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  def anchor[A](a: A): Unit = macro RecorderMacro.anchor[A]

  def exercice(testName: String)(testFun: Unit)(implicit suite: MyFunSuite, anchorRecorder: AnchorRecorder): Unit = macro RecorderMacro.apply

  protected override def runTest(testName: String, args: Args) = {
    // reporter: Reporter, stopper: Stopper, configMap: Map[String, Any], tracker: Tracker) {
    if (!CustomStopper.oneTestFailed) {
      super.runTest(testName, args.copy(reporter = new ReportToTheStopper(args.reporter), stopper = CustomStopper)) // , CustomStopper, configMap, tracker)
    } else {
      SucceededStatus
    }
  }
}
