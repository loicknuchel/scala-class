package exercices

import models.devoxx.{DevoxxApi, Speaker}
import support.HandsOnSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class e05_Future extends HandsOnSuite {

  /**
    * Future[T] est un type conteneur, qui resprente le resultat d'un calcul qui sera disponible dans le futur,
    * elle est programation concurrent et asynchrone
    * elle est souvent utilisée dans les operations lentes : IO (Ecriture sur disque, appel webservice ...) et
    *
    *
    * Future a 2 deux sous-type :
    *   Success => dans le cas ou le calcul s'est deroulé avec succès
    *   Failure => le calcule a échoué

    * Les fonctions :
    *
    *     map : permet d'appliquer une fonction sur la valeur de la Future, et elle retoune une Future
    *     flatMap : appliquer une fonction qui retourne une Future
    *
    */

  // Solutions de l'exercice (en cas de besoin) : https://github.com/loicknuchel/scala-class/blob/solution/src/test/scala/exercices/e05_Future.scala

  exercice("Déclarer une Future") {
    val number = Future {
      Thread.sleep(500)
      42
    }

    val numberValue = Await.result(number, 1 second)
    numberValue shouldBe 42

    number.onComplete {
      case Success(number) => println(number)
      case Failure(ex) => println(s"Erreur de préparation : $ex.getMessage")
    }
  }

  /**
    * map : permet d'appliquer une fonction sur la valeur de de la Future, et elle retoune une Future
    */
  exercice("Appliquer une fonction sur Future") {

    // En utilisant la fonction DevoxxApi.getSpeaker, récupérez le speaker 09a79f4e4592cf77e5ebf0965489e6c7ec0438cd
    val speaker: Future[Speaker] = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")

    // Avec la fontion map, appliquer sur speacker, récupèrer le prénom du speaker
    val firstName: Future[String] = speaker.map(speaker => speaker.firstName)

    val roomName = Await.result(firstName, 1 second)
    roomName shouldBe "Loïc"
  }

  /**
    * zip : permet d'appliquer une fonction sur la valeur de de la Future, et elle retoune une Future
    */
  exercice("Combiner les Futures") {

    // récupérez le speaker 09a79f4e4592cf77e5ebf0965489e6c7ec0438cd et 1693d28c079e6c28269b9aa86ae04a4549ad3074
    val speaker1 = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")
    val speaker2 = DevoxxApi.getSpeaker("1693d28c079e6c28269b9aa86ae04a4549ad3074")

    //En utilisant la méthode zip, combine les deux futures pour en avoir une seule,
    //pui comparrer les langues des deux speakers
    val sameLang = speaker1.zip(speaker2)
      .map { case (speaker1: Speaker, speaker2: Speaker) => speaker1.lang == speaker2.lang }

    val sameLangValue = Await.result(sameLang, 2 second)
    sameLangValue shouldBe true
  }

  /**
    *
    */
  exercice("Le future des Futures") {

    // récupérez le speaker 09a79f4e4592cf77e5ebf0965489e6c7ec0438cd
    val speaker: Future[Speaker] = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")

    // récupèrer les dètails du talk DNY-501
    val speakerNumber: Future[Int] = speaker.flatMap(speaker => DevoxxApi.getTalk(speaker.acceptedTalks.get.head.id))
      .map(talk => talk.speakers.size)

    val speakerNumberValue = Await.result(speakerNumber, 1 second)

    speakerNumberValue shouldBe 3
  }
}
