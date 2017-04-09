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
    * Toutes les fonctions que nous allons voir sont définies dans Traversable et existent donc pour chaque collection.
    * Nous verrons que les collections immuable mais il existe leur équivalent en mutable.
    *
    * Voici la hiérarchie (non-exhaustive) des collections Scala :
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
      * Elle transforme chaque élément en appliquant la fonction passée en paramètre.
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
      * La fonction `find(p: A => Boolean): Option[A]` renvoit le 1er élément pour lequel la fonction en paramètre renvoit `true`
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
    val countries = Map("fr" -> "France", "be" -> "Belgique", "en" -> "Angleterre")

    exercice("manipuler") {
      countries.get("fr") shouldBe __
      countries.map(country => (country._1, country._2.length)) shouldBe __
      countries.map { case (code, name) => (code, name.length) } shouldBe __ // en utilisant le pattern matching \o/
      countries.filter(_._2.contains("r")) shouldBe __
      countries.find(_._2.contains("e")) shouldBe __
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
      words.groupBy(_.length) shouldBe __
      words.groupBy(???)('c') shouldBe List("chaise")
      nums.groupBy(???)("even") shouldBe List(6, 2)
    }

    /**
      * La fonction `flatMap(f: A => List[B]): List[B]` est similaire à la fonction `map`
      * mais fusionne ensemble les listes créées.
      *
      * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#flatMap[B](f:A=>scala.collection.GenTraversableOnce[B]):List[B]
      */
    exercice("flatMap") {
      val words = List("I", "love", "Scala")
      words.map(_.toList) shouldBe __
      words.flatMap(_.toList) shouldBe __

      // Fonctionne aussi avec les Options et toutes les collections
      Some("2").map(toInt) shouldBe __
      Some("2").flatMap(toInt) shouldBe __
      Some("a").map(toInt) shouldBe __
      Some("a").flatMap(toInt) shouldBe __
      None.map(toInt) shouldBe __
      None.flatMap(toInt) shouldBe __

      val nums = List("1", "foo", "3")
      nums.map(toInt) shouldBe __
      nums.flatMap(toInt) shouldBe __
    }

    def toInt(s: String): Option[Int] = {
      try {
        Some(Integer.parseInt(s.trim))
      } catch {
        case _: Exception => None
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

    section("autres") {
      /**
        * `exists(p: A => Boolean): Boolean` indique si au moins un élément vérifie le prédicat
        * C'est l'équivalent de `.find(p).isDefined`
        *
        * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#exists(p:A=>Boolean):Boolean
        */
      exercice("exists") {
        List(1, 2, 3).find(_ > 0).isDefined shouldBe __
        List(1, 2, 3).exists(_ > 0) shouldBe __
        Some(2).find(_ > 0).isDefined shouldBe __
        Some(2).exists(_ > 0) shouldBe __
        Option.empty[Int].exists(_ > 0) shouldBe __
      }

      /**
        * `count(p: A => Boolean): Int` compte le nombre d'éléments qui vérifient le prédicat
        * C'est l'équivalent de `.filter(p).length`
        *
        * cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#count(p:A=>Boolean):Int
        */
      exercice("count") {
        List(1, 2, 3).filter(_ % 2 == 1).length shouldBe __
        List(1, 2, 3).count(_ % 2 == 1) shouldBe __
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

    def frenchPercentageOfTalks(): Int = ???
    def fetchSpeakerTalks(id: SpeakerId): List[Talk] = ???
    def roomSchedule(id: RoomId): List[(Long, Long, TalkId)] = ???

    exercice("fetch") {
      frenchPercentageOfTalks() shouldBe 91
      fetchSpeakerTalks(speakerId).map(_.id) shouldBe List(talkId)
      roomSchedule("par224M-225M").length shouldBe 4
    }
  }
}
