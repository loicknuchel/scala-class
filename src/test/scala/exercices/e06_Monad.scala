package exercices

import models.devoxx.{DevoxxApi, Speaker}
import support.HandsOnSuite

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class e06_Monad extends HandsOnSuite {


  exercice("for-comprehension") {
    val speaker: Future[Speaker] = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")

    val talk: Future[Int] = speaker.flatMap(speaker => DevoxxApi.getTalk(speaker.acceptedTalks.get.head.id))
      .map(talk => talk.speakers.size)
  }
}
