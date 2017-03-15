package exercices

import java.net.MalformedURLException

import models.devoxx.full.DevoxxApi
import support.HandsOnSuite

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class e07_Either extends HandsOnSuite {

  exercice("Either : Right") {
    val speaker = getSpeaker("25b6eeb75c18e3465d5cddf2be297b8863006551")

    speaker.isRight shouldBe true
    speaker.isLeft shouldBe false

    val speakerName: Either[MalformedURLException, String] = speaker.right.map(speaker => speaker.firstName)

    speakerName.right.get shouldBe "Alexandre"
  }


  exercice("Either: Left") {
    val speaker = getSpeaker("toto")

    speaker.isRight shouldBe false
    speaker.isLeft shouldBe true

    val speakerName = speaker.right.map(speaker => speaker.firstName).left.map(ex => ex.getMessage)
  }


  exercice("Either: Combine") {
    val speaker1 = getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")
    val speaker2 = getSpeaker("1693d28c079e6c28269b9aa86ae04a4549ad3074")

    val sameCompany = speaker1.right.flatMap(speaker1 =>
      speaker2.right.map(speaker2 => speaker1.company eq speaker2.company)
    )

    sameCompany.right.get shouldBe false
  }


  def getSpeaker(id: String) = {
    if (id eq "toto")
      Left(new MalformedURLException("le user n'existe pas"))
    else
      Right(Await.result(DevoxxApi.getSpeaker(id), 3 second))
  }
}
