package exercices

import models.devoxx.{DevoxxApi, Speaker}
import support.HandsOnSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class e06_Monad extends HandsOnSuite {


  /**
    * En Scala , enchaîné flatMap suivi de map, est un pattern très récurrent,  alors on un crée for-comprehension,
    * qui est un sucre syntaxique pour enchaîner les opérations de flatMap et map.
    * Attention: un for-comprehension ca n'a rien avoir avec une boucle for
    */
  exercice("for-comprehension") {

    //  Rappelez-vous dans le dernier exercice des Futures, pour récupérer le nombre de speaker, qui donne le meme talk qu'un speaker
    // on a enchaîné les opérations flatMap et map, ce qui parfois devient difficile à lire
    val speaker: Future[Speaker] = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")

    val talk: Future[Int] = speaker.flatMap(speaker => DevoxxApi.getTalk(speaker.acceptedTalks.get.head.id))
      .map(talk => talk.speakers.size)

    // Ici on doit réécrire ce pattern avec for-comprehension
    val speakerNumber = for {
      speaker <- DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")
      talk <- DevoxxApi.getTalk(speaker.acceptedTalks.get.head.id)
    } yield talk.speakers.size

    val speakerNumberValue = Await.result(speakerNumber, 1 second)

    speakerNumberValue shouldBe 3

  }

  /**
    * "Functor"
    *
    * Si vous avez remarqué, Les collections, Future, Option, Try ...ont tous une fonction map, cette fonction prend
    * toujours en paramètre une autre fonction avec la signature A => B, comme suivant :
    *
    *   trait M[A] {
    *     def map[B](f : A => B): M[B]
    *   }
    *
    * Alors le type M définit ce qu’on appelle un “Functor”.
    * Généralisation, Functor est  une structure qui encapsule un type et définit une fonction map
    *
    *
    * "Applicative"
    * Les collections, Future … ont aussi tous une fonction flatMap avec la signature A => M[B]
    * comme suivant :
    * *
    *   trait M[A] {
    *     def flatMap[B](f : A => M[B]): M[B]
    *   }
    * Alors on peut dire que le type M est une “Applicative”.
    *
    *
    * “Monad”  = “Functor” + “Applicative”  c’est tout !
    * Monad est une structure qui encapsule un type, et qui définit deux operation map et flatMap, comme suivant :
    * *
    *   trait M[A] {
    *     def map[B](f : A => B): M[B]
    *     def flatMap[B](f : A => M[B]): M[B]
    *   }
    * L’avantage des structures monadique est qu’elles sont facilement composables, comme on l’a montré dans le for-comprehension
    *
    */
}
