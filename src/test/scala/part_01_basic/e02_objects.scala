package part_01_basic

import support.HandsOnSuite

class e02_objects extends HandsOnSuite {
  /**
    * Scala est bien connu pour son aspect fonctionnel (et nous le verrons un peu plus tard) mais il est tout aussi capable pour l'objet
    * C'est ce que nous allons voir dans cette partie
    */

  exercice("Les classes en Scala") {
    // Les paramètres de la classe seront les paramètres du constructeur et ils seront conservés en tant qu'attribut de la classe
    // s'il n'y a pas de modifieur, l'attribut sera un `private val`
    // si le modifieur est val ou var il sera alors public
    // sinon on peut entièrement le préciser `public var`, `protected val`...
    class Point(var x: Int, var y: Int) {
      def move(dx: Int, dy: Int): Unit = {
        x += dx
        y += dy
      }
    }

    val p = new Point(1, 2)
    p.x shouldBe __
    p.x = 4
    p.x shouldBe __
    p.move(2, 2)
    p.x shouldBe __

    val p1 = new Point(1, 2)
    val p2 = new Point(1, 2)
    // corrige la classe pour que ces assertions soient correctes
    p1 shouldBe p2
    p2.toString shouldBe "Point(1,2)"
  }


  exercice("Ajouter de l'immutabilité") {
    // Comme évoqué au début, l'immutabilité rends les choses beaucoup plus simple

    // - crée une class Point immutable (qu'on ne peut pas modifier)

    // val p = new Point(1, 2)
    // p.x = 3 // ne doit pas compiler


    // - implémente la méthode `move()`

    // val p1 = new Point(1, 2)
    // p1.move(2, 2)
    // p1.x shouldBe 1 // la classe étant immutable elle ne doit pas pouvoir être modifiée


    // - implémente la méthode `copy()` qui permettra de créer une nouvelle classe en modifiant un ou plusieurs paramètres

    // val p2 = p1.copy(3, 4)
    // p2.x shouldBe 3
    // p2.y shouldBe 4
    // val p3 = p1.copy(x = 5)
    // p3.x shouldBe 5
    // p3.y shouldBe 2
    // val p4 = p1.copy(y = 6)
    // p4.x shouldBe 1
    // p4.y shouldBe 6


    // TIP1: une constante ne peut être modifiée
    // TIP2: si on ne peut modifier l'objet, alors il faut en créer un nouveau
    // TIP3: les paramètres nommés et valeurs par défaut seront très utiles ici
  }


  /**
    * Scala propose des `case class` qui sont des classes ordinaires mais avec quelques différences :
    *   - un certain nombre de méthodes directement implémentées :
    *     * equals / hashcode : ils sont basés sur l'égalité structurelle : tous les membres doivent être égaux pour que les classes soient égales
    *     * toString : réimplémenté pour afficher la classe et son contenu plutôt que son adresse
    *     * copy : qui permet de créer une nouvelle classe en modifiant quelques attributs
    *     * eq : permet de tester une égalité de référence
    *   - se construit un peu différemment (cf doc/e02_objects.md) et ne nécessite pas de mot clé `new` à l'instantiation
    *   - les attributs sont `public val` par défaut (au lieu de `private val` pour les classes "normales")
    *
    * Toutes ces particularités rendent les case class très pratiques pour modéliser les données
    */
  exercice("Les case class sont des super classes") {
    case class Point(x: Int, y: Int) {
      def move(dx: Int, dy: Int): Point = Point(x + dx, y + dy)
    }

    val p = Point(1, 2)
    // p.x = 3 // ne compile pas
    p.x shouldBe __
    val p1 = p.move(2, 2)
    p.x shouldBe __
    p1.x shouldBe __

    val p2 = p1.copy(3, 4)
    p2.x shouldBe __
    p2.y shouldBe __
    val p3 = p1.copy(x = 5)
    p3.x shouldBe __
    p3.y shouldBe __
    val p4 = p1.copy(y = 6)
    p4.x shouldBe __
    p4.y shouldBe __
    p4.toString shouldBe __

    // à noter que l'immutabilité obligeant à créer et retourner de nouveaux objets permet d'avoir une API fluent naturellement
    // ex: val result = p.move(3, 4).scale(5).rotate(Point(1, 1), 30)

    // PS: pour aller plus loin avec les case class et comprendre pourquoi on ne met pas de `new` devant, rendez-vous ???
  }


  exercice("Des object pour remplacer static") {
    // TODO
  }


  exercice("Les traits") {
    // TODO
  }


  exercice("Les tuples") {
    // TODO
  }


  exercice("Le pattern matching") {
    // TODO
  }
}
