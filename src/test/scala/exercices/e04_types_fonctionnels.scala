package exercices

import models.devoxx.{DevoxxApi, Speaker, Talk}
import support.HandsOnSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Try}

class e04_types_fonctionnels extends HandsOnSuite {

  /**
    * On commence maintenant à avoir fait un tour sympatique de Scala avec la syntaxe, la partie objet et l'API collection :)
    *
    * A ce propos, lors de ce dernier exercice tu as peut-être remarqué que les collections (List, Map...) et Option
    * partagent un certain nombre de fonctions comme par exemple `map`, `flatMap` ou `filter`...
    *
    * Et bien sûr, ça n'est pas une coïncidence ;)
    * Ces structures (List, Map, Option...) sont en réalité ce qu'on appelle des Monades.
    *
    * Leurs caractéristiques sont :
    *   - être un type wrapper `M[A]` contenant un autre type (cf List[A], Option[A]...)
    *   - avoir une méthode `map(f: A => B): M[B]` permettant de transformer le type contenu
    *   - avoir une méthode `flatMap(f: A => M[B]): M[B]` permettant de conserver le wrapper sur un seul niveau
    *
    * S'il n'y a pas de `map` on parle d'Applicative (wrapper + flatMap)
    * S'il n'y a pas de `flatMap` on parle alors de Fonctor (wrapper + map)
    *
    * C'est pas si compliqué au final ;)
    *
    *
    * Récapitulons donc les différents types monadiques communs en Scala :
    *
    *   List[A]     Option[A]                 Try[A]                          Future[A]             Either[A, B]
    *     |            |                        |                                |                      |
    *   +-+-+       +--+--+             +-------+--------+              +--------+--------+         +---+---+
    *   |   |       |     |             |                |              |                 |         |       |
    *  Nil ::[A]   None Some[A]   Failure[Throwable] Success[A]   Failure[Throwable] Success[A]   Left[A] Right[B]
    *
    *   - List   : contient un nombre intéterminé d'éléments grâce à une structure réccursive
    *   - Option : remplace le `null` et marque la présence ou l'absence d'un élément
    *   - Try    : remplace le lancement d'exceptions en contenant une exception (non lancée) ou un élément
    *   - Future : similaire au Try mais introduit en plus une opération asynchrone et non bloquante (appel réseau, lecture bdd, lecture fichier...)
    *   - Either : contient un élément ou un autre, il sert souvent de Try enrichi (cf exercices)
    *
    * List doc   : http://www.scala-lang.org/api/current/scala/collection/immutable/List.html
    * Option doc : http://www.scala-lang.org/api/current/scala/Option.html
    * Try doc    : http://www.scala-lang.org/api/current/scala/util/Try.html
    * Future doc : http://www.scala-lang.org/api/current/scala/concurrent/Future.html
    * Either doc : http://www.scala-lang.org/api/current/scala/util/Either.html
    */


  section("Manipuler un type monadique") {
    exercice("rappel List et Option") {
      // tansforme "list" pour produire les résultats attendus
      val list: List[String] = List("I", "love", "Monads")
      val lengths: List[Int] = ???
      lengths shouldBe List(1, 4, 6)
      val longests: List[String] = ???
      longests shouldBe List("Monads")
      val letters: List[Char] = ???
      letters shouldBe List('I', 'l', 'o', 'v', 'e', 'M', 'o', 'n', 'a', 'd', 's')

      // tansforme "option" pour produire les résultats attendus
      val option: Option[String] = list.tail.headOption
      val word: Option[String] = ???
      word shouldBe Some("LOVE")
      val long: Option[String] = ???
      long shouldBe None
      val first: Option[Char] = ???
      first shouldBe Some('l')
    }

    exercice("Try") {
      // le constructeur de `Try` catch les exceptions qui pourraient être lancées et construit un Success ou Failure en fonction
      def toInt(str: String): Try[Int] = Try(str.toInt)

      val result: Try[Int] = toInt("12")

      // utilise `map` pour enlever 2 au résultat
      val modified: Try[Int] = ???
      modified shouldBe Success(10)

      // utilise `filter` pour conserver le résultat uniquement s'il est > 10
      val top: Try[Int] = ???
      top shouldBe Success(12)

      // utilise `flatMap` pour transformer à nouveau le résultat obtenu mais avec un '0' en plus
      val twice: Try[Int] = ???
      twice shouldBe Success(120)
    }

    exercice("Future") {
      val future: Future[Speaker] = DevoxxApi.getSpeaker("09a79f4e4592cf77e5ebf0965489e6c7ec0438cd")

      // récupère le `firstName` du speaker grâce à `map`
      val name: Future[String] = ???
      whenReady(name) { result =>
        result shouldBe "Loïc"
      }

      // récupère le speaker uniquement si sa `lang` et "en"
      val isEnglish: Future[Speaker] = ???
      intercept[NoSuchElementException] {
        await(isEnglish)
      }

      // accède au premier talk accepté du speaker (utilise les accesseurs risqués pour simplifier les choses)
      // grâce à DevoxxApi.getTalk(TalkId)
      val firstTalk: Future[Talk] = ???
      whenReady(firstTalk) { result =>
        result.title shouldBe "Scala class, bien démarrer avec Scala"
      }
    }

    /**
      * Comme évoqué en introduction Either sert souvent à renvoyer un résultat (qu'on met par convention à droite, droite = right = correct)
      * ou une erreur (qu'on met à gauche) lorsque l'erreur n'est pas un `Throwable` (impossible d'utiliser Try).
      * Par exemple, si on souhaite valider des données et qu'on veut accumuler les erreurs on pourra utiliser un `Either[List[String], Data]`
      *
      * En Scala 2.11 (utilisé dans ce Hand's on), Either n'est pas une monade mais ses projections le sont.
      *   Il faut alors appliquer `.left` ou `.right` pour obtenir ces projections et les manipuler comme des monades
      * En Scala 2.12, Either est biaisé à droite c'est à dire que par défaut on manipule l'élément de droite.
      *   Il est donc possible de faire un `map` directement, sans utiliser les projections.
      */
    exercice("Either") {
      def toInt(str: String): Either[String, Int] =
        try {
          Right(str.toInt)
        } catch {
          case _: Throwable => Left(s"'$str' in not a number")
        }

      val either: Either[String, Int] = toInt("12")

      // utilise `map` pour enlever 2 au résultat de droite
      val modified: Either[String, Int] = ???
      modified shouldBe Right(10)

      // utilise `filter` pour conserver le résultat à droite uniquement s'il est > 10
      val top: Option[Either[String, Int]] = ???
      top shouldBe Some(Right(12))

      // utilise `flatMap` à droite pour transformer à nouveau le résultat obtenu mais avec un '0' en plus
      val twice: Either[String, Int] = ???
      twice shouldBe Right(120)
    }
  }


  /**
    * Nous venons de le voir, tous ces types partagent beaucoup de choses en commun et ils sont souvent très utilisés.
    * Scala propose donc une syntaxe particulière pour faciliter leur manipulation : le `for-comprehension`
    *
    * Le `for-comprehension` est simplement un peu de sucre syntaxique pour manipuler les monades, à l'exécution le code
    * généré est un assemblage de `map`, `flatMap` et `filter`.
    */
  section("Découverte du for-comprehension") {
    exercice("exemple") {
      val result: Option[String] = for {
        v1 <- Option("value1")
        v2 <- Some((v1, "value2")) if v1.length < 10
        (_, v3) <- Option((v2._1, "value3"))
      } yield v3

      result shouldBe __
    }
    // Remarques :
    //  - on ne peut pas changer de Monade au cours d'un `for-comprehension` (ici on utilise Option)
    //  - il est possible d'utiliser le pattern matching dans la partie résultat

    exercice("mise en application") {
      // récupère le titre du premier talk du speaker "09a79f4e4592cf77e5ebf0965489e6c7ec0438cd" grâce à une for-comprehension
      val firstTalkTitle: Future[String] = ???
      whenReady(firstTalkTitle) { result =>
        result shouldBe "Scala class, bien démarrer avec Scala"
      }
    }
    // Pour voir un exemple un peu plus compliqué, regarde l'implémentation de la méthode DevoxxApi.getModel()
  }
}
