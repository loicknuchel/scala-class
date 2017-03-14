package exercices

import support.HandsOnSuite

import scala.io.Source
import scala.util.{Failure, Success, Try}

class e03_Try extends HandsOnSuite {

  /**
    * TODO
    */


  exercice("Déclarer un Try") {

    val nombreSpeaker = Try {
      Source.fromFile("Speakers.txt").getLines
    }.map(lines => lines.size)

    nombreSpeaker match {
      case Success(count) => print(s"Le fichier Speacker.txt contient : $count speakers")
      case Failure(ex) => println(s"Erreur : ${ex.getMessage}")
    }

    nombreSpeaker.isSuccess shouldBe true
    nombreSpeaker.get shouldBe 2

  }

  exercice("Déclarer un") {

    val nombreTalks = Try {
      Source.fromFile("Talks.txt").getLines
    }.map(lines => lines.size)

    nombreTalks match {
      case Success(count) => print(s"Le fichier Speacker.txt contient : $count speakers")
      case Failure(ex) => println(s"Erreur : ${ex.getMessage}")
    }

    nombreTalks.failed shouldBe true
    nombreTalks.recover{ case _ => 0} shouldBe 0

  }


  }