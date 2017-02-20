package part_01_basic

import org.scalatest.{FunSuite, Matchers}
import support.StopAfterFailure

class e02_collections extends FunSuite with Matchers with StopAfterFailure {
  test("aaa") {
    assert("a" == "a")
  }

  test("bbb") {
    assert("z" == "a")
  }

  test("ccc") {
    assert("a" == "a")
  }
}
