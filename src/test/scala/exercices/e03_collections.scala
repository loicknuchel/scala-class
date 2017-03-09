package exercices

import org.joda.time.DateTime
import support.HandsOnSuite

class e03_collections extends HandsOnSuite {
  /**
    * Passons maintenant aux collections qui sont souvent au coeur de notre code
    *
    * En Scala, toutes les collections étendent le trait `Traversable`
    * Toutes les fonctions que nous allons voir sont définies dans Traversable et sont donc accessible pour n'importe quelle collection.
    * Scala et la programmation fonctionnelle mettent en avant l'immutabilité mais il existe aussi l'équivalent avec des collections mutables.
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
    * Plus d'infos : http://docs.scala-lang.org/overviews/collections/overview.html
    */

  /**
    * Les listes Scala sont composés de deux classes : `Nil` et `::` (appelé Cons).
    * Le premier étant la liste vide et le second un élément contenant une valeur (head) et une liste (tail).
    * Mais le plus souvent nous n'interragissons pas directement avec cette implémentation...
    *
    * cf http://www.scala-lang.org/api/current/index.html#scala.collection.immutable.List
    */
  exercice("Manipuler une liste immutable") {
    val numbers: List[Int] = List(2, 4, 6)
    val other: List[Int] = List(2, 4, 6)

    // opérations basiques
    numbers.head shouldBe __ // manière risquée d'accéder au premier élément
    numbers.tail shouldBe __ // manière risquée d'accéder à la liste sans le premier élément
    numbers(1) shouldBe __ // manière risquée d'accéder à un élément

    // comme les case class, les collections ont une égalité structurelle
    numbers == other shouldBe __
    numbers eq other shouldBe __

    // Nil
    // append elt
    // headOption

    // La fonction `.map(f: A => B): List[B]` sert à transformer les éléments d'une liste
    // c'est une fonction d'ordre suppérieur car elle prend une autre fonction en paramètre
    // cf https://www.scala-lang.org/api/current/scala/collection/immutable/List.html#map[B](f:A=>B):List[B]
    numbers.map(n => n.toString) shouldBe __
    numbers.map(???) shouldBe List(4, 8, 12)

    // La fonction `.filter(p: A => Boolean): List[A]` sert à sélectionner certains éléments d'une liste
    // le résultat sera une liste avec uniquement les éléments qui auront renvoyé `true`
    numbers.filter(n => n > 3) shouldBe __
    numbers.filter(_ > 3) shouldBe __
    // le `_` dans une lambda représente le paramètre de la fonction
    // c'est très pratique lorsque les expressions sont simple cependant il n'est pas toujours possible de l'utiliser, par exemple lors d'une double condition

    // La fonction `.find(p: A => Boolean): Option[A]` sert trouver un élément dans une liste
    // elle renvoit le premier élément qui renvoit `true` pour la condition
    // on note qu'elle ne renvoit pas l'élément mais une Option[A] car l'élément peut être présent ou pas (cf l'exercice suivant)
    numbers.find(_ == 2).get shouldBe __
    numbers.find(_ > 3).get shouldBe __
  }


  /**
    * L'une des erreurs les plus difficiles à éviter est le bien connu NullPointerException
    * Une solution très simple à ce problème est de simplement interdire les `null`...
    * C'est ce que fait Scala en proposant le type Option[A] pour représenter l'absence de valeur
    * (null existe toujours pour la compatibilité JVM mais il ne doit JAMAIS être utilisé !)
    *
    * Le type Option[A] est une classe abstraite qui comporte deux sous-types : Some[A] (si l'élément est présent) et None (sinon)
    *
    *    Option[A]
    *       |
    *   +---+---+
    *   |       |
    * Some[A]  None
    *
    * Ses principales fonctions sont :
    *   - .isEmpty / .nonEmpty      : pour tester si l'élément est présent ou non
    *   - .get                      : pour accéder à l'élément de manière risquée (lance une exception dans le cas de None)
    *   - .getOrElse(default: A)    : pour accéder à l'élément ou à une valeur par défaut en cas de None
    *   - .orElse(other: Option[A]) : permet de "concaténer" une autre option (le premier élément sinon le deuxième sinon None)
    *   - .map(f: A => B)           : pour transformer le contenu sans l'extraire (sans effet dans le cas de None)
    *   - toutes les fonctions des collections (similaire à une liste d'un seul élément)
    *
    * cf: https://www.scala-lang.org/api/current/scala/Option.html
    */
  exercice("Option") {
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
    assertThrows[NoSuchElementException] {
      lastName.get
    }
  }


  /**
    * Comme dans beaucoup de langages Scala a une collection Map[A, B] qui associe une clé A à une valeur B
    * Map étant une collection, les méthodes qu'on a vu s'appliques comme pour les listes
    */
  exercice("Map") {
    val states = Map("fr" -> "France", "be" -> "Belgique", "en" -> "Angleterre")
    states.get("fr") shouldBe __
    // l'élément du .map() est un tuple avec la clé et la valeur
    states.map(state => (state._1, state._2.length)) shouldBe __
    // on le verra plus tard mais il est possible d'extraire directement les éléments du tuple grâce au pattern matching :)
    states.map { case (code, name) => (code, name.length) } shouldBe __

    // .toList & .toMap
  }


  /**
    * L'API des collections en Scala est très riche et permet de manipuler facilement les données
    * Voici quelques fonctions très utiles mais n'hésitez pas à consulter l'API complète : http://www.scala-lang.org/api/current/scala/collection/Traversable.html
    */
  exercice("Toujours plus de manipulation de List") {
    val words = List("table", "chaise", "bureau", "écran", "ordinateur")

    val wordsByLength: Map[Int, List[String]] = words.groupBy(_.length)
    wordsByLength.get(5) shouldBe Some(List("table", "écran"))

    // group by
    // flatMap
    // reduce
  }


  /**
    * Voyons comment mettre en pratique ces manipulations de données
    */
  exercice("Mise en application") {
    import models.devoxx.basic._
    val talks = List(
      Talk("BBV-277", Conference, "Le bon testeur il teste...", "Pourquoi proposer une nouvelle conférence sur les tests ?", List("6cbb41adbc049f923c4327ed3642f208faf4e03f", "5926b150dbddc5ae5214ad045de64d806306ed67"))
    )
    val speakers = List(
      Speaker("6cbb41adbc049f923c4327ed3642f208faf4e03f", "Agnès", "Crepet", "fr"),
      Speaker("5926b150dbddc5ae5214ad045de64d806306ed67", "Guillaume", "Ehret", "fr")
    )

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
  }
}
