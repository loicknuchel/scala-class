package support

import org.scalatest.{FunSuite, Matchers}

trait HandsOnSuite extends FunSuite with Matchers with StopAfterFailure {
  lazy val __ : Any = null
}
