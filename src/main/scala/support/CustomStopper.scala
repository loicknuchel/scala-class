package support

import org.scalatest.Stopper

object CustomStopper extends Stopper {
  var oneTestFailed = false

  def stopRequested: Boolean = oneTestFailed

  def requestStop(): Unit = {
    oneTestFailed = true
    ()
  }
}
