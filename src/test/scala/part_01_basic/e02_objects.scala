package part_01_basic

import support.HandsOnSuite

import scala.util.Random

class e02_objects extends HandsOnSuite {
  /**
    * Scala est bien connu pour son aspect fonctionnel (et nous le verrons un peu plus tard) mais il est tout aussi capable pour l'objet
    * C'est ce que nous allons voir dans cette partie
    */

  test("Les classes en Scala") {
    // Les paramètres de la classe seront les paramètres du constructeur et ils seront conservés en tant qu'attribut de la classe
    // s'il n'y a pas de modifieur, l'attribut sera un `private val`
    // si le modifieur est val ou var il sera alors public
    // sinon on peut entièrement le préciser `public var`, `protected val`...
    class Point(var x: Int, var y: Int) {
      def move(dx: Int, dy: Int): Unit = {
        x += dx
        y += dy
      }
    }

    val p = new Point(1, 2)
    p.x shouldBe __
    p.x = 4
    p.x shouldBe __
    p.move(2, 2)
    p.x shouldBe __

    val p1 = new Point(1, 2)
    val p2 = new Point(1, 2)
    // corrige la classe pour que ces assertions soient correctes
    p1 shouldBe p2
    p2.toString shouldBe "Point(1,2)"
  }

  test("Ajouter de l'immutabilité") {
    // Comme évoqué au début, l'immutabilité rends les choses beaucoup plus simple

    // - crée une class Point immutable (qu'on ne peut pas modifier)

    // val p = new Point(1, 2)
    // p.x = 1 // ce code ne doit pas compiler


    // - implémente la méthode `move()`

    // val p1 = new Point(1, 2)
    // p1.move(2, 2)
    // p1.x shouldBe 1 // la classe étant immutable elle ne doit pas pouvoir être modifiée


    // - implémente la méthode `copy()` qui permettra de créer une nouvelle classe en modifiant un ou plusieurs paramètres

    // val p2 = new Point(1, 2)


    // TIP1: une constante ne peut être modifiée
    // TIP2: si on ne peut modifier l'objet, alors il faut en créer un nouveau
    // TIP3: les paramètres nommés et valeurs par défaut seront très utiles ici
  }

  test("Les case class sont des super classes") {
    // Le constructuer d'une case class est très similaire à celui d'une classe
    // sauf qui si les modifieurs ne sont pas précisé, l'attribut sera en `public val`
    case class Point(x: Int, y: Int)
    println("p1: "+Point(1, 2))

    // PS: pour aller plus loin avec les case class et comprendre pourquoi on ne met pas de `new` devant, rendez-vous ???
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
