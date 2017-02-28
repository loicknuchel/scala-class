package part_01_basic

import support.HandsOnSuite

import scala.util.Random

class e02_objects extends HandsOnSuite {
  /**
    * Scala est bien connu pour son aspect fonctionnel (et nous le verrons un peu plus tard) mais il est tout aussi capable pour l'objet
    * C'est ce que nous allons voir dans cette partie
    */

  test("Les classes en Scala") {
    // class parameters are constructor parameters
    //  - no modifier : private val
    //  - val/var : public val/var
    //  - your modifier
    class Point(var x: Int, var y: Int, z: Int = Random.nextInt(10)) {
      def move(dx: Int, dy: Int): Unit = {
        x += dx
        y += dy
      }
      def guessZ(value: Int): Boolean = value == z
      def changeZ(dz: Int): Unit = {
        z += 1
      }
    }

    val p = new Point(1, 2, 3)
    //p.x shouldBe __
  }


  /**
    * class
    * case class
    * object
    * trait
    * tuple
    * extractor / pattern matching
    * sealed trait
    */
}
