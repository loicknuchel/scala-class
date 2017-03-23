package bonus

import support.HandsOnSuite

import scala.io.Source
import scala.util.{Failure, Success, Try}

class e04_Try extends HandsOnSuite {
  /**
    * Try[T] est un  type qui encapsule des opérations qui peuvent lancer
    * des exceptions (IO , JDBCException ….).
    *
    * Try a deux sous-type
    *   Success[T] => si l'opération s'est déroulé avec succès
    *   Failure[T] => si l'opération a lancé une exception
    *
    * Les fonctions :
    *     isSuccess : retourne true si l'opération est Succes sinon false
    *     failed : retourne false si l'opération est Succes sinon true
    *     map : permet d'appliquer une fonction sur la valeur du Try et retoune un Try
    *     flatMap : appliquer une fonction qui retourne un Try
    */

  // Solutions de l'exercice (en cas de besoin) : https://github.com/loicknuchel/scala-class/blob/solution/src/test/scala/bonus/e04_Try.scala
  exercice("Déclarer un Try : Succes") {

    //Lire le fichier README.md
    val linesCount = Try {
      Source.fromFile("README.md").getLines
    }.map(lines => lines.size)

    // L'opération s'est déroulé avec succès ?
    linesCount.isSuccess shouldBe true
    //README.md contient combien de ligne ?
    linesCount.get shouldBe 30


    // Patter matching : retounez le nombre de lignes si c'est ok sinon 0
    val count = linesCount match {
      case Success(count) => count
      case Failure(ex) => 0
    }

    count shouldBe 30

  }


  exercice("Failled") {

    val nombreTalks = Try {
      Source.fromFile("TOTO.txt").getLines
    }.map(lines => lines.size)

    //Sachant que le fichier TOTO.txt n'existe pas, l'opération sera Succes ou Failed ?
    nombreTalks.failed shouldBe true
    nombreTalks.recover { case _ => 0 } shouldBe 0
  }
}
