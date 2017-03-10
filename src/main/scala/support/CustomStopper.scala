package support

import org.scalatest.Stopper

object CustomStopper extends Stopper {
  var oneTestFailed = false

  def requestStop() {
    testFailed
  }

  def stopRequested = oneTestFailed

  def testFailed: Unit = {
    oneTestFailed = true
    ()
  }
}
