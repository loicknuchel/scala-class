package bonus

import java.net.MalformedURLException

import models.devoxx.{DevoxxApi, Speaker}
import support.HandsOnSuite

class e05_Either extends HandsOnSuite {

  // Solutions de l'exercice (en cas de besoin) : https://github.com/loicknuchel/scala-class/blob/solution/src/test/scala/bonus/e05_Either.scala

  exercice("Either : Right") {
    val speaker = getSpeaker("25b6eeb75c18e3465d5cddf2be297b8863006551")

    speaker.isRight shouldBe __
    speaker.isLeft shouldBe __

    val speakerName: Either[MalformedURLException, String] = speaker.right.map(speaker => speaker.firstName)

    speakerName.right.get shouldBe __
  }


  exercice("Either: Left") {
    val speaker = getSpeaker("toto")

    speaker.isRight shouldBe __
    speaker.isLeft shouldBe __

    val speakerName = speaker.right.map(speaker => speaker.firstName).left.map(ex => ex.getMessage)
  }


  exercice("Either: Combine") {
    val speaker1 = getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")
    val speaker2 = getSpeaker("1693d28c079e6c28269b9aa86ae04a4549ad3074")

    val sameCompany = speaker1.right.flatMap(speaker1 =>
      speaker2.right.map(speaker2 => speaker1.company eq speaker2.company)
    )

    sameCompany.right.get shouldBe __
  }


  def getSpeaker(id: String): Either[MalformedURLException, Speaker] = {
    if (id eq "toto")
      Left(new MalformedURLException("le user n'existe pas"))
    else
      Right(await(DevoxxApi.getSpeaker(id)))
  }
}
