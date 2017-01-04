package part_01_basic

import org.scalatest.{Matchers, FunSuite}

/**
  * Prerequisites: please read the README.md file to install all necessary tools
  */

/**
  * Introduction
  *
  * Scala is a JVM based language which promote functional programming style as well as object oriented programming style; and they play well together !
  * You can write Scala code very similar to Java one but it’s not a really the best idea. We will see how to write good Scala code leveraging best parts of FP and OOP.
  * As a guide, if the code is immutable, there are chances it is not so bad ;)
  */

/**
  * Side-note:
  * In Scala you can use `???` to express a missing piece of code.
  * It's just a shorthand for `throw new NotImplementedError("an implementation is missing")` which can replace any code and satisfy the compiler.
  * In this class we will use them a lot and you have to replace them to progress
  */

class e01_scala_syntax extends FunSuite with Matchers {
  lazy val __ : Any = ???
  /**
    * var, val & def are Scala keywords to respectively declare variables, constants and functions
    */
  test("Declare some basic Scala objects") {
    var variable: Int = 5
    val constant: String = "abc"
    def function(x: Int, y: Int): String = {
      return (x + y).toString()
    }

    variable = 42 // variables can be reassigned with same type
    //constant = "def" // constants can't be reassigned (immutable FTW !)
    val result = function(variable, 1)

    variable shouldBe 42 // TODO __
    constant shouldBe "abc" // TODO __
    result shouldBe "43" // TODO __
  }

  /**
    * Scala language try to be as concise as possible, so it has some feature for that :
    *   - type inference: you don't need to specify types everywhere, the compiler can guess them
    *   - everything is an expression: so you don't need return keyword in function on even {}
    *   - () and . are optionals
    */
  test("Let's minimize boilerplate") {
    var i = 4 // inferred as Int
    // i = "abc" // so this doesn't compile

    def add(a: Int, b: Int) = a + b // return type inferred as Int, {} are not required if there is just one expression and neither the `return` keyword
    // var a: String = add(1, 2) // so this doesn't compile

    var j = if(i == 4) "abc" else "def" // as everything is an expression, you can affect the return type of a `if` statement to a variable

    var k = 12 toString // methods taking no parameters don't need () and you can ommit the `.` (which is helpful for DSL but you should not abuse of it)
  }

  /**
    * Optional & named parameters
    */

  /**
    * Class / trait
    */

  /**
    * _ shorthand
    */
}
