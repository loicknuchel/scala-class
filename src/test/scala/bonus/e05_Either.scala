package bonus

import java.net.MalformedURLException

import models.devoxx.{DevoxxApi, Speaker}
import support.HandsOnSuite

class e05_Either extends HandsOnSuite {

  /**
    *Either[A, B] est un type, qui encapsule deux valeur de Type A et B, B (droit) représente la valeur calculée,
    * A (gauche) représente un message d’erreur
    * Either a deux sous-types :
    *       Right[B] =>  si le calcul s'est déroulé avec succès
    *       Left[A] => si l'opération a lancé une exception, A peut être un String pour un simple message d'erreur
    *       ou une exception de type MalformedURLException, IOException ....
    *
    *  Example : getSpeaker a la fin de la classe, retourne une type Either[MalformedURLException, Speaker] :
    *        si le résultat est ok => le type de retour est Right[Speaker]
    *        si le résultat est ko => le type de retour est Left[MalformedURLException]

    */

  // Solutions de l'exercice (en cas de besoin) : https://github.com/loicknuchel/scala-class/blob/solution/src/test/scala/bonus/e05_Either.scala

  exercice("Either : Right") {
    // récupérez le speaker 1693d28c079e6c28269b9aa86ae04a4549ad3074
    val speaker = getSpeaker("1693d28c079e6c28269b9aa86ae04a4549ad3074")

    // Sachant que le résultat de l'appel est ok, le speaker est à droit ou à gauche?
    speaker.isRight shouldBe __
    speaker.isLeft shouldBe __

    //map peut être appliqué  à droit ou à gauche du Either
    val speakerName: Either[MalformedURLException, String] = speaker.right.map(speaker => speaker.firstName)

    //Depuis la variable speakerName , récupérez le nom du speaker
    __ shouldBe "Fabrice"
  }


  exercice("Either: Left") {
    val speaker = getSpeaker("toto")

    // Sachant que le speaker toto n'existe pas, le speaker est à droit ou à gauche?
    speaker.isRight shouldBe __
    speaker.isLeft shouldBe __
  }

  /**
    * on peut combiner facilement des Eithers grâce à  la fonction flatMap
    */
  exercice("Either: Combine") {
    val speaker1 = getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")
    val speaker2 = getSpeaker("1693d28c079e6c28269b9aa86ae04a4549ad3074")

    val sameCompany: Either[MalformedURLException, Boolean] = speaker1.right.flatMap(speaker1 =>
      speaker2.right.map(speaker2 => speaker1.company eq speaker2.company)
    )

    __ shouldBe false
  }


  def getSpeaker(id: String): Either[MalformedURLException, Speaker] = {
    if (id eq "toto")
      Left(new MalformedURLException("le user n'existe pas"))
    else
      Right(await(DevoxxApi.getSpeaker(id)))
  }
}
