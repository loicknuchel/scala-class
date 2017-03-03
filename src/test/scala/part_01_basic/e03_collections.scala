package part_01_basic

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
    // map
    // _ shorthand
    // filter
  }

  /**
    * Hiérarchie pour Option :
    *
    *    Option
    *      |
    *   +--+--+
    *   |     |
    * Some   None
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
