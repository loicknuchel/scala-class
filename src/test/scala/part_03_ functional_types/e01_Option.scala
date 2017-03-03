
import support.HandsOnSuite
import org.scalatest.Matchers._

import scala.None

class e01_Option extends HandsOnSuite {

  /**
    * Option
    *
    *  Option[T] en scala est un conteneur d'une valeur optionelle de type T
    *  Option a valeur
    *     * Some[T] => presente le cas ou la valeur est presente
    *     * None    => presente l'absence de la valeur
    *
    */
  exercice("Déclarer une Option") {
    // pour declarer une Option on peut utiliser le constructeur de la classe Some
    val age : Option[Int] = Some(42)

    age.get shouldBe 42

    //S'il ya un doute sur la nulleté de la valeur on peut utiliser le constructeur de Option
    val valeurNull : String = null
    val valeurAbsente : Option[String] = Option(valeurNull)

    valeurAbsente  shouldBe None

  }

  /**
    * Comme dans les collections, Option a une fontion Map qui permet d'appliquer une fontion sur la valeur de option
    * ainsi l'application de la fonction de type A => B sur une Option[A] retournera une Option[B]
    * */

  exercice("Appliquer une fonction sur une Option") {
    // En utilisant la fontion map incrementer l'age
    val age : Option[Int] = Some(42)
    val ageIncremente = age.map(x => x+1)

    ageIncremente.get shouldBe 43

    // appliquer la meme fonction sur une None , quel sera le resultat de retour ?

    val ageAbsent : Option[Int] = None
    val ageAbsentIncremente = ageAbsent.map(x => x + 1)

    ageAbsentIncremente shouldBe None
  }

  exercice("flatMap sur une Option") {

  }

  exercice("Pattern matching sur une Option") {

  }
}