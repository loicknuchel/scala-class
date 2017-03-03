
import support.HandsOnSuite
import org.scalatest.Matchers._

import scala.None

class e01_Option extends HandsOnSuite {

  /**
    * Option
    *
    *  Option[A] en scala est un conteneur de valeur optionelle de type T
    *  Option a valeur
    *     * Some[A] => presente le cas ou la valeur est presente
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

    valeurAbsente shouldBe None

  }
  /**
    * Dans le cas d'absence de valeur, la fonction getOrElse permet de retourner un valeur dpar defaut
    */

  exercice("Valeur par défaut") {

    val age : Option[Int] = None

    age shouldBe None
    age.getOrElse(42) shouldBe 42
  }

  /**
    * Comme pour les collections, Option a une fontion Map qui permet d'appliquer une fontion sur la valeur de option
    * ainsi l'application de la fonction de type A => B il existe deux  cas :
    *          * si Some[A], le resultat sera une Some[B]
    *          * si None le resultat sera un None
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

  /**
    * Option possède aussi une fonction flatMap, qui prend une fonction de type A => Option[B]
    * et comme dans le resultat de retour est:
    *          * si Some[A], le resultat sera une Some[B]
    *          * si None le resultat sera un None
    */

  exercice("flatMap sur une Option") {
    // En utilisant la fontion map incrementez l'age
    val age : Option[Int] = Some(42)
    val ageIncremente = age.flatMap(x => Option(x+1))

    ageIncremente.get shouldBe 43

    // appliquer la meme fonction sur une None , quel sera le resultat de retour ?

    val ageAbsent : Option[Int] = None
    val ageAbsentIncremente = ageAbsent.flatMap(x => Option(x+1))

    ageAbsentIncremente shouldBe None
  }
  /**
    * Some est une case class, alors il est possible d'appliquer du pattern matching
    */
  exercice("Pattern matching sur une Option") {
    Some("toto") match {
      case Some(valeur) => print()
      case None => print()
    }
  }
}