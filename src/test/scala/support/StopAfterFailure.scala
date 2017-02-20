package support

import org.scalatest._
import scala.collection.mutable.ListBuffer

trait StopAfterFailure extends SuiteMixin { this: Suite =>
  val statusList = new ListBuffer[Status]()
  var failed = false

  override def runTests(testName : Option[String], args: Args): Status = {
    import args._

    val stopRequested = stopper
    // If a testName is passed to run, just run that, else run the tests returned
    // by testNames.
    testName match {
      case Some(tn) => runTest(tn, args)
      case None =>
        val tests = testNames.iterator
        for (test <- tests) {
          if (failed == false) {
            val status = runTest(test, args)
            statusList += status
            failed = !status.succeeds()
          }
        }
        new CompositeStatus(Set.empty ++ statusList)
    }
  }
}
