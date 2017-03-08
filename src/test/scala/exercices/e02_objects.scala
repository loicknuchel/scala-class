package exercices

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
    // corrige la classe Point pour que ces assertions soient correctes
    p1 == p2 shouldBe true
    p2.toString shouldBe "Point(1,2)"

    // TIP1: look at hashCode/equals, intellij can generate them...
  }


  exercice("Ajouter de l'immutabilité") {
    // Comme évoqué au début, l'immutabilité rends les choses beaucoup plus simple

    // - crée une class Point immutable (qu'on ne peut pas modifier)

    // val p = new Point(1, 2)
    // p.x = 3 // ne doit pas compiler


    // - implémente la méthode `move()`

    // p.move(2, 1)
    // p.x shouldBe 1 // la classe étant immutable elle ne doit pas pouvoir être modifiée

    // val p1 = p.move(2, 1)
    // p1.x shouldBe 3


    // - implémente la méthode `copy()` qui permettra de créer une nouvelle classe en modifiant un ou plusieurs paramètres

    // val p2 = p.copy(3, 4)
    // p2.x shouldBe 3
    // p2.y shouldBe 4
    // val p3 = p.copy(x = 5)
    // p3.x shouldBe 5
    // p3.y shouldBe 2
    // val p4 = p.copy(y = 6)
    // p4.x shouldBe 1
    // p4.y shouldBe 6


    // TIP1: une valeur ne peut être modifiée
    // TIP2: si on ne peut modifier l'objet, alors il faut en créer un nouveau
    // TIP3: les paramètres nommés et valeurs par défaut seront très utiles ici
  }


  /**
    * Scala propose des `case class` qui sont des classes ordinaires mais avec quelques différences :
    *   - un certain nombre de méthodes sont directement implémentées :
    *     * equals / hashcode : ils sont basés sur l'égalité structurelle : tous les membres doivent être égaux pour que les classes soient égales
    *     * toString          : affiche la classe et son contenu plutôt que son adresse
    *     * copy              : permet de créer une nouvelle classe en modifiant quelques attributs
    *     * eq                : permet de tester une égalité de référence
    *   - ne nécessite pas de mot clé `new` à l'instantiation (cf bonus)
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

    val p1 = p.move(2, 1)
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

    // à noter que l'immutabilité obligeant à créer et retourner de nouveaux objets permet d'avoir une fluent API naturellement
    // ex: val result = p.move(3, 4).scale(5).rotate(Point(1, 1), 30)

    // PS: pour aller plus loin avec les case class et comprendre pourquoi on ne met pas de `new` devant, rendez-vous ???
  }


  /**
    * Le langage Scala n'a pas de concept de `static`. A la place il propose des `object` aussi nommés objet companions lorsqu'ils accompagnent une case class
    * Ce sont simplement des singletons (instance unique) qui peuvent être appelés depuis n'importe quel code.
    */
  exercice("Des 'object' pour remplacer static") {

    object Utils {
      def apply(x: Int): Int = x
      def toUpper(str: String): String = str.toUpperCase
    }

    Utils.toUpper("test") shouldBe __

    // on trouve souvent les object associés à des case class pour contenir les fonctions utilitaires non liées à l'instance de la classe
    object User {
      def fromFullName(fullName: String): User = {
        val parts = fullName.split(" ")
        User(parts.head, parts.tail.mkString(" "))
      }
    }
    case class User(firstName: String, lastName: String)

    val user = User.fromFullName("Jean Dupont")
    user shouldBe __

    // une pratique intéressante de programmation fonctionnelle est de coder les méthodes d'une classe comme des fonctions d'un objet companion
    // ça permet de réutiliser plus simplement le code et d'avoir une fonction pure facilement testable
    // cette fonction peut soit prendre la classe en paramètre, soit des paramètres plus basiques (et donc plus génériques)
    object Person {
      def initials(p: Person): String = (p.firstName.substring(0, 1) + p.lastName.substring(0, 1)).toLowerCase
      def trigramme(firstName: String, lastName: String): String = (firstName.substring(0, 1) + lastName.substring(0, 2)).toLowerCase
    }
    case class Person(firstName: String, lastName: String) {
      def initials(): String = Person.initials(this)
      def trigramme(): String = Person.trigramme(firstName, lastName)
    }

    val person = Person("Jeanne", "Michu")
    person.initials shouldBe __
    person.trigramme shouldBe __
    Person.trigramme("Jean-Claude", "Convenant") shouldBe __
    Person.trigramme(user.firstName, user.lastName) shouldBe __
  }


  /**
    * Les traits Scala sont très similaires aux interfaces Java 8
    */
  exercice("Les traits") {
    // abstract val & def, concrete methods (like Java 8 interfaces)
    // can hold state (not Java 8 interfaces)
    // can be used for multiple inheritance
    
    // TODO
  }


  exercice("Les tuples") {
    // TODO
    // pair._1
  }


  exercice("Le pattern matching") {
    // TODO
  }
}
