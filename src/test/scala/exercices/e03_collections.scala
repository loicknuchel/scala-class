package exercices

import models.devoxx._
import support.HandsOnSuite

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class e03_collections extends HandsOnSuite {

  // Solutions de l'exercice (en cas de besoin) : https://github.com/loicknuchel/scala-class/blob/solution/src/test/scala/exercices/e03_collections.scala

  /**
    * Passons maintenant aux collections...
    *
    * En Scala, toutes les collections étendent le trait `Traversable`
    * Toutes les fonctions que nous allons voir sont définies dans Traversable et existent donc pour chaque quelle collection.
    * Nous verrons que les collections immuable mais il existe leur équivalent en mutable.
    *
    * Voici la hierarchie (non-exhaustive) des collections Scala :
    *
    *                                            Traversable
    *                                                |
    *                                             Iterable
    *                                                |
    *                  +-----------------------------+--------------------------+
    *                  |                             |                          |
    *                 Seq                           Map                        Set
    *                  |                             |                          |
    *         +--------+----------+          +-------+--------+         +-------+--------+
    *         |                   |          |       |        |         |       |        |
    *     LinearSeq           IndexedSeq  HashMap ListMap SortedMap  HashSet ListSet SortedSet
    *         |                   |                           |                          |
    *   +-----+-----+      +------+-----+                     |                          |
    *   |     |     |      |      |     |                     |                          |
    * List Stream Queue Vector String Range                TreeMap                    TreeSet
    *
    * cf http://docs.scala-lang.org/overviews/collections/overview.html
    */


  /**
    * Les listes Scala sont composés de deux classes : `Nil` et `::` (appelé Cons).
    * `Nil` étant la liste vide et `::` un élément contenant une valeur (head) et une liste (tail), éventuellement vide.
    * Les listes Scala sont donc définies comme une structure réccursive.
    * Mais le plus souvent nous n'interragissons pas directement avec cette spécificité d'implémentation...
    *
    * cf http://www.scala-lang.org/api/current/index.html#scala.collection.immutable.List
    */
  section("Liste immuable") {
    val empty: List[Int] = Nil
    val numbers: List[Int] = List(2, 4, 6)
    val other: List[Int] = List(2, 4, 6)

    /**
      * comme pour les case class, les collections ont une égalité structurelle
      */
    exercice("comparaison") {
      numbers == other shouldBe true
      numbers eq other shouldBe false
      empty == numbers shouldBe false
      empty == numbers.tail.tail.tail shouldBe true
    }

    exercice("opérations basiques") {
      numbers(1) shouldBe 4 // manière risquée d'accéder à un élément
      numbers.head shouldBe 2 // manière risquée d'accéder au premier élément
      numbers.tail shouldBe List(4, 6) // manière risquée d'accéder à la liste sans le premier élément
      intercept[NoSuchElementException] {
        empty.head
      }
      intercept[UnsupportedOperationException] {
        empty.tail
      }
      intercept[IndexOutOfBoundsException] {
        empty(1)
      }
    }

    /**
      * La fonction `map(f: A => B): List[B]` crée une nouvelle liste.
      * Elle transforme chaque élément en appliquant la fonction passée en paramètre.
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#map[B](f:A=>B):List[B]
      */
    exercice("transformer les éléments") {
      numbers.map(n => n.toString) shouldBe List("2", "4", "6")
      numbers.map(n => n * 2) shouldBe List(4, 8, 12)
    }

    /**
      * La fonction `filter(p: A => Boolean): List[A]` crée une nouvelle liste.
      * Elle conserve que les éléments pour lesquels la fonction en paramètre a renvoyé `true`
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#filter(p:A=>Boolean):Repr
      */
    exercice("sélectionner des éléments") {
      numbers.filter(n => n > 3) shouldBe List(4, 6)
      numbers.filter(_ < 5) shouldBe List(2, 4)
    }
    // le `_` dans une lambda représente le paramètre unique de la fonction
    // c'est très pratique lorsque les expressions sont simples cependant il n'est pas toujours possible de l'utiliser

    /**
      * La fonction `find(p: A => Boolean): Option[A]` renvoit le 1er élément pour lequel la fonction en paramètre renvoit `true`
      * Comme on ne peut pas être certain de trouver un élément qui correspond, le retour est une Option (cf exercice suivant)
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#find(p:A=>Boolean):Option[A]
      */
    exercice("chercher un élément") {
      numbers.find(_ == 2).get shouldBe 2
      numbers.find(_ > 3).get shouldBe 4
    }
  }


  /**
    * Nous venons de l'évoquer, parfois on a besoin de représenter la présence ou l'absence de valeur.
    * Beaucoup de langages utilisent `null` ou `undefined` pour ça, voire les deux !!!
    * Ce qui crée la célèbre NullPointerException en Java ou le "Cannot read property 'x' of undefined" en JavaScript :(
    * (cf https://github.com/search?q=NullPointerException&type=Commits)
    *
    * Scala a fait le choix de ne pas utiliser `null` (même s'il est présent pour la compatibilité Java).
    * Il y a donc un type spécifique pour gérer cet usage.
    * Je vous présente donc le type : Option !!!
    *
    * Il permet donc :
    *   - d'éliminer complètement toute erreur du type NullPointerException à l'exécution !!!
    *   - de rendre explicite ce qui peut être absent et ce qui ne peut pas l'être
    *   - éviter les vérifications inutiles qui polluent le code
    *
    * Le type Option[A] est une classe abstraite qui comporte deux sous-types :
    *   - Some[A] : si l'élément est présent
    *   - None    : si l'élément est absent
    *
    * Ses principales méthodes sont :
    *   - isEmpty / nonEmpty       : tester si l'élément est présent ou non
    *   - get                      : accéder à l'élément de manière risquée (lance une exception dans le cas de None)
    *   - getOrElse(default: A)    : accéder à l'élément ou à une valeur par défaut en cas de None
    *   - orElse(other: Option[A]) : "concaténer" avec une autre option (le premier élément sinon le deuxième sinon None)
    *   - map(f: A => B)           : transformer l'élément sans l'extraire (sans effet dans le cas de None)
    *   - toutes les fonctions des collections (similaire à une liste à un seul élément)
    *
    * cf: https://www.scala-lang.org/api/current/scala/Option.html
    */
  section("Option") {
    exercice("manipuler") {
      val firstName = Some("Jean")

      firstName.isEmpty shouldBe false
      firstName.nonEmpty shouldBe true
      firstName.getOrElse("empty") shouldBe "Jean"
      firstName.orElse(Some("other")) shouldBe Some("Jean")
      firstName.map(_.length) shouldBe Some(4)
      firstName.get shouldBe "Jean"

      val lastName: Option[String] = None

      lastName.isEmpty shouldBe true
      lastName.nonEmpty shouldBe false
      lastName.getOrElse("empty") shouldBe "empty"
      lastName.orElse(Some("other")) shouldBe Some("other")
      lastName.map(_.length) shouldBe None
      intercept[NoSuchElementException] {
        lastName.get
      }
    }
  }


  /**
    * Comme dans beaucoup de langages, Scala a une collection Map[A, B] qui associe une clé A à une valeur B
    * Map étant une collection, les méthodes qu'on a vu s'appliquent comme pour les listes
    * L'élément est un tuple `(A, B)` au lieu de simplement `A` dans le cas d'une `List[A]`
    * Il est d'ailleurs très facile de passer de `Map[A, B]` à `List[(A, B)]` et inversement grâce à `toList` et `toMap`
    *
    * cf https://www.scala-lang.org/api/current/scala/collection/Map.html
    */
  section("Map") {
    val states = Map("fr" -> "France", "be" -> "Belgique", "en" -> "Angleterre")

    exercice("manipuler") {
      states.get("fr") shouldBe Some("France")
      states.map(state => (state._1, state._2.length)) shouldBe Map("fr" -> 6, "be" -> 8, "en" -> 10)
      states.map { case (code, name) => (code, name.length) } shouldBe Map("fr" -> 6, "be" -> 8, "en" -> 10) // en utilisant le pattern matching \o/
      states.filter(_._2.contains("r")) shouldBe Map("fr" -> "France", "en" -> "Angleterre")
      states.find(_._2.contains("e")) shouldBe Some("fr" -> "France")
    }
  }

  /**
    * L'API collection Scala est très riche et permet de manipuler les données simplement
    * Nous allons voir quelques méthodes intéressantes mais pensez à jeter un oeil à l'API complète...
    *
    * cf http://www.scala-lang.org/api/current/scala/collection/Traversable.html
    */
  section("API collection") {
    /**
      * La fonction `groupBy(f: A => K): Map[K, List[A]]` crée une Map.
      * Le résultat de la fonction en paramètre est utilisé comme clé
      * et la valeur est une liste des éléments correspondants à cette clé
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#groupBy[K](f:A=>K):scala.collection.immutable.Map[K,Repr]
      */
    exercice("groupBy") {
      val words = List("table", "chaise", "bureau", "écran", "ordinateur")
      val nums = List(1, 5, 6, 7, 2)
      words.groupBy(_.length) shouldBe Map(
        5 -> List("table", "écran"),
        6 -> List("chaise", "bureau"),
        10 -> List("ordinateur")
      )
      words.groupBy(_.head)('c') shouldBe List("chaise")
      nums.groupBy(n => if (n % 2 == 0) "even" else "odd")("even") shouldBe List(6, 2)
    }

    /**
      * La fonction `flatMap(f: A => List[B]): List[B]` est similaire à la fonction `map`
      * mais fusionne ensemble les listes créées.
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#flatMap[B](f:A=>scala.collection.GenTraversableOnce[B]):List[B]
      */
    exercice("flatMap") {
      val words = List("I", "love", "Scala")
      words.map(_.toList) shouldBe List(List('I'), List('l', 'o', 'v', 'e'), List('S', 'c', 'a', 'l', 'a'))
      words.flatMap(_.toList) shouldBe List('I', 'l', 'o', 'v', 'e', 'S', 'c', 'a', 'l', 'a')

      // Fonctionne aussi avec les Options et toutes les collections
      val nums = List("1", "foo", "3")
      nums.map(toInt) shouldBe List(Some(1), None, Some(3))
      nums.flatMap(toInt) shouldBe List(1, 3)

      Some("2").map(toInt) shouldBe Some(Some(2))
      Some("2").flatMap(toInt) shouldBe Some(2)
      Some("a").map(toInt) shouldBe Some(None)
      Some("a").flatMap(toInt) shouldBe None
      None.map(toInt) shouldBe None
      None.flatMap(toInt) shouldBe None
    }

    def toInt(s: String): Option[Int] = {
      try {
        Some(Integer.parseInt(s.trim))
      } catch {
        case e: Exception => None
      }
    }

    /**
      * La fonction `fold(z: A)(op: (A, A) => A): A` permet d'aggréger les éléments d'une collection
      * `z` est la valeur initiale
      * `op` est l'opération d'agrégation avec en paramètres l'accumulateur et la valeur courante.
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#fold[A1>:A](z:A1)(op:(A1,A1)=>A1):A1
      */
    exercice("fold") {
      val words = List("I", "love", "Scala")
      words.fold("") { case (acc, word) => acc + " " + word } shouldBe " I love Scala"
      // si la lambda prends deux paramètres, alors on peut utiliser deux `_` comme raccourci...
      words.fold("")(_ + _.length.toString) shouldBe "145"
    }

    section("autres") {
      /**
        * `exists(p: A => Boolean): Boolean` indique si au moins un élément vérifie le prédicat
        * C'est l'équivalent de `.find(p).isDefined`
        *
        * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#exists(p:A=>Boolean):Boolean
        */
      exercice("exists") {
        List(1, 2, 3).find(_ > 0).isDefined shouldBe true
        List(1, 2, 3).exists(_ > 0) shouldBe true
        Some(2).find(_ > 0).isDefined shouldBe true
        Some(2).exists(_ > 0) shouldBe true
        Option.empty[Int].exists(_ > 0) shouldBe false
      }

      /**
        * `count(p: A => Boolean): Int` compte le nombre d'éléments qui vérifient le prédicat
        * C'est l'équivalent de `.filter(p).length`
        *
        * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#count(p:A=>Boolean):Int
        */
      exercice("count") {
        List(1, 2, 3).filter(_ % 2 == 1).length shouldBe 2
        List(1, 2, 3).count(_ % 2 == 1) shouldBe 2
      }
    }
  }


  /**
    * Voyons comment mettre en pratique ces manipulations de données...
    *
    * Essaie d'en faire le plus possible mais ne passe pas plus de 15 min sur cet exercice.
    * Remplace 'exercice("")' par 'ignore("")' pour aller à la suite.
    */
  section("Mise en pratique") {
    // pour voir les attributs de ces classes, regarde le package `models.devoxx` ou utilise le 'Ctrl + Click' dans ton IDE
    // pour voir des exemples de données, regarde dans le répertoire `src/test/resources/devoxx-api`
    val (speakers: List[Speaker], talks: List[Talk], rooms: List[Room], slots: List[Slot]) = Await.result(DevoxxApi.getModel(), 5 second)

    val talkId = "XPI-0919"
    val talkTitle = "Scala class, bien démarrer avec Scala"
    val speakerId = "09a79f4e4592cf77e5ebf0965489e6c7ec0438cd"

    def frenchPercentageOfTalks(): Int = Math.round(100 * talks.count(_.lang == "fr").toFloat / talks.length.toFloat)
    def fetchTalk(id: TalkId): Option[Talk] = talks.find(_.id == id)
    def fetchSpeakerTalks(id: SpeakerId): List[Talk] = speakers.find(_.uuid == id).map { speaker =>
      talks.filter(talk => speaker.acceptedTalks.exists(_.exists(_.id == talk.id)))
    }.getOrElse(List())
    def talkSpeakers(id: TalkId): List[Speaker] = fetchTalk(id).map { talk =>
      speakers.filter(speaker => talk.speakers.exists(_.link.href.contains(speaker.uuid)))
    }.getOrElse(List())
    def roomSchedule(id: RoomId): List[(Long, Long, TalkId)] =
      slots.filter(_.roomId == id).flatMap { slot =>
        slot.talk.map { talk =>
          (slot.fromTimeMillis, slot.toTimeMillis, talk.id)
        }
      }
    def findSpeaker(id: SpeakerId, time: Long): Option[Room] =
      slots
        .filter(s => s.fromTimeMillis < time && time < s.toTimeMillis)
        .find { slot =>
          slot.talk.exists { talk =>
            talk.speakers.exists(_.link.href.contains(id))
          }
        }
        .flatMap(s => rooms.find(_.id == s.roomId))

    exercice("fetch") {
      frenchPercentageOfTalks() shouldBe 90
      fetchTalk(talkId).map(_.title) shouldBe Some(talkTitle)
      fetchSpeakerTalks(speakerId).map(_.id) shouldBe List(talkId)
      talkSpeakers(talkId).map(_.firstName).sorted shouldBe List("Fabrice", "Loïc", "walid")
      roomSchedule("par224M-225M").length shouldBe 5
      findSpeaker(speakerId, 1491491200000L).map(_.id) shouldBe Some("par224M-225M")
    }
  }
}
