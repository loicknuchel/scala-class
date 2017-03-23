package bonus

import support.HandsOnSuite

import scala.io.Source
import scala.util.{Failure, Success, Try}

class e04_Try extends HandsOnSuite {

  // Solutions de l'exercice (en cas de besoin) : https://github.com/loicknuchel/scala-class/blob/solution/src/test/scala/bonus/e04_Try.scala

  /**
    * Try[T] est un  type qui encapsule des opérations qui peuvent lancer
    * des exceptions (IO , JDBCException...).
    *
    * Try a deux sous-type
    *   Success[T] => si l'opération s'est déroulé avec succès
    *   Failure[T] => si l'opération a lancé une exception
    *
    * Les fonctions :
    *     isSuccess : retourne true si l'opération est un succès sinon false
    *     isFailure : retourne false si l'opération est un succès sinon true
    *     map : permet d'appliquer une fonction sur la valeur du Try et retoune un Try
    *     flatMap : appliquer une fonction qui retourne un Try
    */
  exercice("Déclarer un Try : Succès") {
    //Lire le fichier README.md
    val linesCount = Try {
      Source.fromFile("README.md").getLines
    }.map(lines => lines.size)

    // L'opération s'est déroulée avec succès ?
     __ shouldBe true

    // README.md contient combien de ligne ?
    __ shouldBe 22

    // Pattern matching: retournez le nombre de lignes si c'est ok sinon 0
    val count: Int = linesCount match {
      case Success(valeur) => ???
      case Failure(err) => ???
    }

    count shouldBe 22
  }


  exercice("Failed") {
    val linesCount = Try {
      Source.fromFile("TOTO.txt").getLines
    }.map(lines => lines.size)

    // Sachant que le fichier TOTO.txt n'existe pas, l'opération sera un Success ou une Failure ?
    __ shouldBe true
    linesCount.recover { case _ => 0 } shouldBe __
  }
}
