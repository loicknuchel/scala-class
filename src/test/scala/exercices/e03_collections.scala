package exercices

import support.HandsOnSuite

class e03_collections extends HandsOnSuite {
  /**
    * List
    * map
    *   _
    * filter
    * find
    *   Option
    * groupBy
    *   Map
    * flatMap
    * reduce
    *
    * bonus: covariance
    */

  /**
    * Passons maintenant aux collections qui sont souvent au coeur de notre code
    *
    * En Scala, toutes les collections étendent le trait `Traversable`
    * Toutes les fonctions que nous allons voir sont définies dans Traversable et sont donc accessible pour n'importe quelle collection.
    * Scala et la programmation fonctionnelle mettent en avant l'immutabilité mais il existe aussi l'équivalent avec des collections mutables.
    *
    * Voici la hierarchie (non-exhaustive) des collections Scala :
    *
    *                                               Traversable
    *                                                   |
    *                                                Iterable
    *                                                   |
    *                  +--------------------------------+--------------------------+
    *                  |                                |                          |
    *                 Seq                              Map                        Set
    *                  |                                |                          |
    *         +--------+----------+             +-------+--------+         +-------+--------+
    *         |                   |             |       |        |         |       |        |
    *     LinearSeq           IndexedSeq     HashMap ListMap SortedMap  HashSet ListSet SortedSet
    *         |                   |                              |                          |
    *   +-----+-----+      +------+-----+                        |                          |
    *   |     |     |      |      |     |                        |                          |
    * List Stream Queue Vector String Range                   TreeMap                    TreeSet
    *
    * Plus d'infos : http://docs.scala-lang.org/overviews/collections/overview.html
    */

  /**
    * Les listes Scala sont composés de deux classes : `Nil` et `::`.
    * Le premier étant la liste vide et le second un élément contenant une valeur (head) et une liste (tail).
    * Mais le plus souvent nous n'interragissons pas directement avec cette implémentation...
    *
    * L'API List : http://www.scala-lang.org/api/current/index.html#scala.collection.immutable.List
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
    * Une Option[A] est un type qui représente la présence ou l'absence d'un élément (de type A)
    * C'est ce qui est utilisé en Scala pour éviter les `null` et les exceptions
    *
    * Le type Option[A] est une classe abstraite qui comporte deux sous-types : Some[A] (si l'élément est présent) et None (sinon)
    *
    *      Option
    *        |
    *    +---+---+
    *    |       |
    * Some[A]   None
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
    // Option
    // no null
  }

  exercice("Toujours plus de manipulation de List") {
    // group by
    // Map
    // Map.map : ._1, case
    // flatMap
  }
}
