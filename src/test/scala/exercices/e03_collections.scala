package exercices

import models.devoxx._
import support.HandsOnSuite

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class e03_collections extends HandsOnSuite {

  /**
    * Passons maintenant aux collections...
    *
    * En Scala, toutes les collections étendent le trait `Traversable`
    * Toutes les fonctions que nous allons voir sont définies dans Traversable et sont donc accessible pour n'importe quelle collection.
    * Scala et la programmation fonctionnelle mettent en avant l'immuabilité mais il existe aussi un équivalent avec des collections mutables.
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
      numbers == other shouldBe __
      numbers eq other shouldBe __
      empty == numbers shouldBe __
      empty == numbers.tail.tail.tail shouldBe __
    }

    exercice("opérations basiques") {
      numbers(1) shouldBe __ // manière risquée d'accéder à un élément
      numbers.head shouldBe __ // manière risquée d'accéder au premier élément
      numbers.tail shouldBe __ // manière risquée d'accéder à la liste sans le premier élément
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
      * Elle transforme chaque élément à l'aide de la fonction passée en paramètre.
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#map[B](f:A=>B):List[B]
      */
    exercice("transformer les éléments") {
      numbers.map(n => n.toString) shouldBe __
      numbers.map(???) shouldBe List(4, 8, 12)
    }

    /**
      * La fonction `filter(p: A => Boolean): List[A]` crée une nouvelle liste.
      * Elle conserve que les éléments pour lesquels la fonction en paramètre a renvoyé `true`
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#filter(p:A=>Boolean):Repr
      */
    exercice("sélectionner des éléments") {
      numbers.filter(n => n > 3) shouldBe __
      numbers.filter(_ < 5) shouldBe __
    }
    // le `_` dans une lambda représente le paramètre unique de la fonction
    // c'est très pratique lorsque les expressions sont simples cependant il n'est pas toujours possible de l'utiliser

    /**
      * La fonction `find(p: A => Boolean): Option[A]` renvoit le premier élément pour lequel la fonction en paramètre renvoit `true`
      * Comme on ne peut pas être certain de trouver un élément qui correspond, le retour est une Option (cf exercice suivant)
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#find(p:A=>Boolean):Option[A]
      */
    exercice("chercher un élément") {
      numbers.find(_ == 2).get shouldBe __
      numbers.find(_ > 3).get shouldBe __
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

      firstName.isEmpty shouldBe __
      firstName.nonEmpty shouldBe __
      firstName.getOrElse("empty") shouldBe __
      firstName.orElse(Some("other")) shouldBe __
      firstName.map(_.length) shouldBe __
      firstName.get shouldBe __

      val lastName: Option[String] = None

      lastName.isEmpty shouldBe __
      lastName.nonEmpty shouldBe __
      lastName.getOrElse("empty") shouldBe __
      lastName.orElse(Some("other")) shouldBe __
      lastName.map(_.length) shouldBe __
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
      states.get("fr") shouldBe __
      states.map(state => (state._1, state._2.length)) shouldBe __
      states.map { case (code, name) => (code, name.length) } shouldBe __ // en utilisant le pattern matching \o/
      states.filter(_._2.contains("r")) shouldBe __
      states.find(_._2.contains("e")) shouldBe __
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
      * Le résultat de la fonction en paramètre est utilisé comme clé et la valeur est une liste des éléments correspondant à cette clé
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#groupBy[K](f:A=>K):scala.collection.immutable.Map[K,Repr]
      */
    exercice("groupBy") {
      val words = List("table", "chaise", "bureau", "écran", "ordinateur")
      val nums = List(1, 5, 6, 7, 2)
      words.groupBy(_.length) shouldBe __
      words.groupBy(???)('c') shouldBe List("chaise")
      nums.groupBy(???)("even") shouldBe List(6, 2)
    }

    /**
      * La fonction `flatMap(f: A => List[B]): List[B]` est similaire à la fonction `map` mais fusionne ensemble les listes créées.
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#flatMap[B](f:A=>scala.collection.GenTraversableOnce[B]):List[B]
      */
    exercice("flatMap") {
      val words = List("I", "love", "Scala")
      words.map(_.toList) shouldBe __
      words.flatMap(_.toList) shouldBe __

      // Fonctionne aussi avec les Options et toutes les collections
      val nums = List("1", "foo", "3")
      nums.map(toInt) shouldBe __
      nums.flatMap(toInt) shouldBe __

      Some("2").map(toInt) shouldBe __
      Some("2").flatMap(toInt) shouldBe __
      Some("a").map(toInt) shouldBe __
      Some("a").flatMap(toInt) shouldBe __
      None.map(toInt) shouldBe __
      None.flatMap(toInt) shouldBe __
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
      words.fold("") { case (acc, word) => acc + " " + word } shouldBe __
      // si la lambda prends deux paramètres, alors on peut utiliser deux `_` comme raccourci...
      words.fold("")(_ + _.length.toString) shouldBe __
    }

    exercice("autres") {
      // TODO : exists pour les listes et options
    }
  }


  /**
    * Voyons comment mettre en pratique ces manipulations de données
    */
  section("Mise en pratique") {
    val (speakers: List[Speaker], talks: List[Talk], rooms: List[Room], slots: List[Slot]) = Await.result(DevoxxApi.getModel(), 5 second)

    val talkId = "XPI-0919"
    val talkTitle = "Scala class, bien démarrer avec Scala"
    val speakerId = "09a79f4e4592cf77e5ebf0965489e6c7ec0438cd"

    def fetchTalk(id: TalkId): Option[Talk] = ???
    def fetchSpeakerTalks(id: SpeakerId): List[Talk] = ???
    def talkSpeakers(id: TalkId): List[Speaker] = ???
    // pourcentage de talks en français
    // l'emploi du temps d'une salle

    exercice("fetch") {
      fetchTalk(talkId).map(_.title) shouldBe Some(talkTitle)
      fetchSpeakerTalks(speakerId).map(_.id) shouldBe List(talkId)
      talkSpeakers(talkId).map(_.firstName).sorted shouldBe List("Fabrice", "Loïc", "walid")
    }
  }

  /*section("Mise en pratique") {
    // récupérer la liste des speakers pour un talk
    def getTalkSpeakers(talk: Talk, speakers: List[Speaker]): List[Speaker] = {
      speakers.filter(s => talk.speakers.contains(s.uuid))
    }

    getTalkSpeakers(talks.head, speakers).map(_.uuid) shouldBe talks.head.speakers

    // associer les talks à leur salle
    def getRoomTalks(slots: List[Slot], talks: List[Talk]): Map[RoomId, List[Talk]] = {
      slots.groupBy(_.room).map { case (id, roomSlots) =>
        (id, roomSlots.flatMap(s => talks.find(_.id == s.talk)))
      }
    }

    // extraire la liste des salles et horaires pour un speaker
    def speakerSchedule(slots: List[Slot], talks: List[Talk], speaker: SpeakerId): List[(DateTime, DateTime, RoomId)] = {
      slots.filter { s =>
        talks
          .find(_.id == s.talk)
          .exists(t => t.speakers.contains(speaker))
      }.map(s => (s.from, s.to, s.room))
    }

    // déterminer où se trouve un speaker à une heure précise
    def whereIsCharlie(slots: List[Slot], talks: List[Talk], speaker: SpeakerId, time: DateTime): Option[RoomId] = {
      speakerSchedule(slots, talks, speaker)
        .find { case (from, to, room) => from.isBefore(time) && to.isAfter(time) }
        .map(_._3)
    }
  }*/
}
