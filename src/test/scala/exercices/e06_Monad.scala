package exercices

import models.devoxx.{DevoxxApi, Speaker}
import support.HandsOnSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class e06_Monad extends HandsOnSuite {

  // Solutions de l'exercice (en cas de besoin) : https://github.com/loicknuchel/scala-class/blob/solution/src/test/scala/exercices/e06_Monad.scala

  /**
    * En Scala, enchaîner flatMap suivi de map, est un pattern très récurrent, il existe donc le for-comprehension
    * qui est un sucre syntaxique pour enchaîner des opérations de flatMap et map.
    * Attention: un for-comprehension ca n'a rien avoir avec une boucle for
    */
  exercice("for-comprehension") {
    //  Rappelez-vous dans le dernier exercice des Futures, pour récupérer le nombre de speakers, qui donnent le même talk qu'un speaker
    // on a enchaîné les opérations flatMap et map, ce qui parfois peut devenir difficile à lire

    // Ancien code :

    val speaker: Future[Speaker] = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")
    val talk: Future[Int] = speaker
      .flatMap(speaker => DevoxxApi.getTalk(speaker.acceptedTalks.get.head.id))
      .map(talk => talk.speakers.size)

    // Ici on peut réécrire ce pattern avec un for-comprehension
    val speakerNumber: Future[Int] = for {
      speaker <- DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")
      talk <- DevoxxApi.getTalk(speaker.acceptedTalks.get.head.id)
    } yield ???

    val speakerNumberValue = await(speakerNumber)
    speakerNumberValue shouldBe 3
  }


  /**
    * "Functor"
    *
    * Si vous avez remarqué, Les collections, Future, Option, Try... ont toutes une fonction map, cette fonction prend
    * toujours en paramètre une autre fonction avec la signature A => B, comme suivant :
    *
    *   trait M[A] {
    *     def map[B](f : A => B): M[B]
    *   }
    *
    * Le type M définit ce qu’on appelle un “Functor”.
    * Un Functor est une structure qui encapsule un type et définit une fonction map
    *
    *
    * "Applicative"
    *
    * De même, beaucoup de types ont aussi une fonction flatMap qui prends en paramètre une fonction A => M[B]
    * comme suivant :
    *
    *   trait M[A] {
    *     def flatMap[B](f : A => M[B]): M[B]
    *   }
    *
    * Alors on peut dire que le type M est une “Applicative”.
    *
    *
    * “Monad”  = “Functor” + “Applicative”  c’est tout !
    * Une Monade est une structure qui encapsule un type, et qui définit deux opérations map et flatMap, comme suivant :
    *
    *   trait M[A] {
    *     def map[B](f : A => B): M[B]
    *     def flatMap[B](f : A => M[B]): M[B]
    *   }
    *
    * L’avantage des structures monadiques est qu’elles sont facilement composables, comme on l’a montré dans le for-comprehension
    */
}
