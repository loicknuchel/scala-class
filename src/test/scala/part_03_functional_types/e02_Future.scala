
import models.devoxx.full.{DevoxxApi, Speaker}
import support.HandsOnSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class e02_Future extends HandsOnSuite {

  /**
    * TODO
    */

  exercice("Déclarer une Future") {

    val number = Future {
      Thread.sleep(500)
      42
    }

    val numberValue = Await.result(number , 1 second)
    numberValue shouldBe  42

    number.onComplete {
      case Success(number) => println(number)
      case Failure(ex) => println(s"Erreur de préparation : $ex.getMessage")
    }
  }

  exercice("Appliquer une fonction sur Future") {

    // récupèrer le speaker 09a79f4e4592cf77e5ebf0965489e6c7ec0438cd
    val speaker: Future[Speaker] = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd") // DevoxxService.getSlotByTalkId("DNY-501")

    // récupèrer le prénom du speaker
    val firstName: Future[String] = speaker.map(speaker => speaker.firstName)

    val roomName = Await.result(firstName , 1 second)
    roomName shouldBe  "Loïc"
  }

  exercice("Combiner les Futures") {

    val speaker1 = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")
    val speaker2 = DevoxxApi.getSpeaker("1693d28c079e6c28269b9aa86ae04a4549ad3074")

    //combiner les deux futures pour en avoir une seul
    val description = speaker1.zip(speaker2)
            .map{case (speaker1 : Speaker , speaker2 : Speaker) => speaker1.lang == speaker2.lang}

    val descriptionValue = Await.result(description, 2 second)
    descriptionValue shouldBe  true
  }


  exercice("Le future des Futures") {

    // récupèrer le slot du talk DNY-501
    val speaker: Future[Speaker] = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd") // DevoxxService.getSlotByTalkId("DNY-501")

    // récupèrer les dètails du talk DNY-501
    val talk :Future[Int] = speaker.flatMap(speaker => DevoxxApi.getTalk(" DNY-501"))
                                      .map(talk => talk.speakers.size)

    val talkValue = Await.result(talk , 1 second)

    talkValue shouldBe 3

  }
}