package exercices

import models.devoxx.{DevoxxApi, Speaker}
import support.HandsOnSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.util.{Failure, Success}

class e05_Future extends HandsOnSuite {

  // Solutions de l'exercice (en cas de besoin) : https://github.com/loicknuchel/scala-class/blob/solution/src/test/scala/exercices/e05_Future.scala

  /**
    * Future[T] est un type conteneur, qui resprésente le résultat d'un calcul qui sera disponible dans le futur,
    * ce type est très utilisé pour la programation concurrente et asynchrone,
    * en particulier pour des opérations lentes : IO (écriture sur disque, appel webservice ...)
    *
    * Future a deux sous-type :
    *   Success => dans le cas où le calcul s'est déroulé avec succès
    *   Failure => quand le calcul a échoué
    *
    * Les fonctions :
    *
    *     map : permet d'appliquer une fonction sur la valeur de la Future, et elle retoune une Future
    *     flatMap : appliquer une fonction qui retourne une Future
    */
  exercice("Déclarer une Future") {
    val number = Future {
      Thread.sleep(500)
      42
    }

    val numberValue = await(number)
    numberValue shouldBe __

    //Callback :  afficher le nombre dans le cas de Succès et exception dans le cas de Failure
    number.onComplete {
      case Success(value) => ???
      case Failure(err) => ???
    }
  }


  /**
    * map : permet d'appliquer une fonction sur la valeur de la Future, et elle retoune une Future
    */
  exercice("Appliquer une fonction sur Future") {
    // En utilisant la fonction DevoxxApi.getSpeaker, récupérez le speaker 09a79f4e4592cf77e5ebf0965489e6c7ec0438cd
    val speaker: Future[Speaker] = ???

    // Avec la fonction map, appliquée sur speaker, récupérez le prénom du speaker
    val firstName: Future[String] = ???

    val roomName = await(firstName)
    roomName shouldBe "Loïc"
  }


  /**
    * zip : permet d'appliquer une fonction sur la valeur de la Future, et elle retoune une Future
    */
  exercice("Combiner les Futures") {
    // récupérez le speaker 09a79f4e4592cf77e5ebf0965489e6c7ec0438cd et 1693d28c079e6c28269b9aa86ae04a4549ad3074
    val speaker1 = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")
    val speaker2 = DevoxxApi.getSpeaker("1693d28c079e6c28269b9aa86ae04a4549ad3074")

    // En utilisant la méthode zip, combine les deux futures pour en avoir une seule,
    // puis comparre les langues des deux speakers
    val sameLang: Future[Boolean] = ???

    val sameLangValue = await(sameLang)
    sameLangValue shouldBe true
  }

  /**
    * flatMap est une opération qui permet de combiner des Futures
    */
  exercice("Le future des Futures") {
    // récupérez le speaker 09a79f4e4592cf77e5ebf0965489e6c7ec0438cd
    val speaker: Future[Speaker] = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")

    // récupérez les details du premier Talk de ce speaker avec flatMap, puis
    // retournez le nombre de speakers de ce talk
    val speakerNumber: Future[Int] = ???

    val speakerNumberValue = await(speakerNumber)

    speakerNumberValue shouldBe 3
  }
}
