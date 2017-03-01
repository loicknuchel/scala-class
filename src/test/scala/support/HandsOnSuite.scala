package support

import org.scalatest.{FunSpec, Matchers}

trait HandsOnSuite extends FunSpec with Matchers {
  lazy val __ : Any = null
  val exercice = it
}
