package exercices

import support.HandsOnSuite

class e02_objects extends HandsOnSuite {

  /**
    * Scala est bien connu pour son aspect fonctionnel (et nous le verrons un peu plus tard) mais il est tout aussi capable pour l'objet
    * C'est ce que nous allons voir dans cette partie
    */


  ignore("Les classes en Scala") {
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

    // TIP1: penser à hashCode/equals, intellij peut les générer...
  }


  exercice("Ajouter de l'immutabilité") {
    // Comme évoqué au début, l'immutabilité rends les choses beaucoup plus simple

    // - crée une class Point immutable qui réponde à ces assertions

    "Crée la classe Point et décommente les lignes ci-dessous" shouldBe __

    // val p = new Point(1, 2)
    // p.x = 3 // ne doit pas compiler


    // - implémente la méthode `move()` de la classe Point

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


    // TIP1: une valeur (val) ne peut être modifiée
    // TIP2: si on ne peut pas modifier un objet, alors il faut en créer un nouveau
    // TIP3: les paramètres nommés et valeurs par défaut seront très utiles ici
  }


  /**
    * Scala propose des `case class` qui sont des classes ordinaires avec certaines méthodes implémentées par défaut :
    *   - méthodes déjà implémentées :
    *     - equals / hashcode : ils sont basés sur l'égalité structurelle : tous les membres doivent être égaux pour que les classes soient égales
    *     - toString          : affiche la classe et son contenu plutôt que son adresse
    *     - copy              : permet de créer une nouvelle classe en modifiant quelques attributs
    *     - eq                : permet de tester une égalité de référence
    *   - ne nécessite pas de mot clé `new` à l'instantiation (cf bonus)
    *   - les attributs sont `public val` par défaut (au lieu de `private val` pour les classes "normales")
    *
    * Toutes ces particularités rendent les case class très pratiques pour modéliser les données d'une application
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

    // PS: pour aller plus loin avec les case class et comprendre pourquoi on ne met pas de `new` devant, rendez-vous dans les bonus
    // Il est possible de lancer le handson avec les bonus avec la commande `./handons bonus`
  }


  /**
    * Le langage Scala n'a pas de `static`. A la place il propose des `object` aussi nommés objet companions lorsqu'ils accompagnent une case class
    * Ce sont simplement des singletons (instance unique) qui peuvent être appelés depuis n'importe quel code.
    */
  exercice("Des 'object' pour remplacer static") {
    object Utils {
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
    * Les traits Scala sont très similaires aux interfaces de Java 8.
    * Ils peuvent contenir des variables, des valeurs et/ou des méthodes. Celles-ci peuvent être abstraites ou concrètes.
    * Il est aussi possible d'hériter de plusieurs traits (comme une interface) contrairement aux classes
    */
  exercice("Les traits") {
    trait Geo {
      // valeur abstraite, doit être définie dans la classe qui implémente ce trait
      val name: String

      // méthode abstraite, doit être définie par la classe qui implémente ce trait
      def perimeter(): Double

      // variable concrète, accessible dans la classe qui implémente ce trait
      var color = "red"

      // méthode concrète, accessible dans la classe qui implémente ce trait
      def setColor(c: String): Unit = this.color = c
    }
    case class Square(width: Double) extends Geo {
      val name = "Square"

      def perimeter(): Double = width * 4
    }

    val square = Square(5)
    square.name shouldBe __
    square.perimeter() shouldBe __
    square.color shouldBe __
    square.setColor("blue")
    square.color shouldBe __

    val other = Square(4)
    other.color shouldBe __
  }


  /**
    * Parfois, on a besoin d'une fonction qui renvoit plusieurs résultats.
    * Dans ce cas, on peut créer un objet spécifique avec ces différents résultats ou utiliser un objet générique qui peut contenir plusieurs valeurs.
    * Par exemple en Java, on créer souvent un objet Pair<A, B> (ex: https://github.com/search?q=filename%3APair.java)
    *
    * En Scala, on a les tuples qui sont des case class qui peuvent contenir un nombre de valeurs hétérogènes fixe.
    * Les tuples sont disponibles de 1 à 22 paramètres.
    */
  exercice("Les tuples") {
    val pair: Tuple2[Int, String] = (1, "test")
    pair._1 shouldBe __
    pair._2 shouldBe __

    // n'importe quelle valeur peut être dans un tuple
    val multi = (3, 2.0, "1", (n: Int) => n + 1)
    multi._3 shouldBe __
    multi._4(multi._1) shouldBe __
  }


  /**
    * Un extracteur est l'inverse d'un constructeur.
    * Il permet d'extraire les valeurs d'un objet.
    */
  exercice("Extractors") {
    // On extrait les valeurs du tuple dans deux variables indépendantes
    val pair = (2, "name")
    val (value, name) = pair
    value shouldBe __
    name shouldBe __

    // Et bien sûr il est possible de généraliser ça au delà des tupes grâce à la méthode `unapply()`
    class User(val name: String, val score: Int)
    class Point(val x: Int, val y: Int)
    object MyExtractor {
      def unapply(arg: User): Option[(String, Int)] = Some((arg.name, arg.score))

      def unapply(arg: Point): Option[(Int, Int)] = Some((arg.x, arg.y))
    }

    // On appelle la méthode unapply de l'extracteur identifié
    // Si une valeur ne nous intéresse pas, on peut utiliser `_` pour ne pas l'affecter
    val MyExtractor(username, _) = new User("Jean", 10)
    username shouldBe __
    val MyExtractor(x, y) = new Point(1, 2)
    x shouldBe __

    // Bien souvent on va créer l'extracteur dans l'objet companion de la classe
    class Square(val width: Int)
    object Square {
      def unapply(arg: Square): Option[Int] = Some(arg.width)
    }
    val Square(width) = new Square(3)
    width shouldBe __

    // Pour le cas des `case class` la méthode `unapply()` des automatiquement générée
    case class Circle(x: Int, y: Int, r: Int)
    val Circle(_, _, r) = Circle(3, 3, 5)
    r shouldBe __

    // A noter quand même, la méthode unapply renvoyant une option de tuple, on est limité à 22 paramètres par les tuples :(
  }


  /**
    * Le pattern matching est un mécanisme similaire aux `switch` d'autres langages mais en étant bien plus souple.
    * Par ailleurs, comme en Scala toute expression retroue une valeur, c'est aussi le cas du pattern matching
    */
  exercice("Le pattern matching") {
    // Commençons par du classique...
    val r1 = "B" match {
      case "A" => "it's A"
      case "B" => "B wins"
      case "C" => "C is best"
    }
    r1 shouldBe __

    // On peut ajouter un catch-all avec un case "sans condition"
    // c'est le cas des deux derniers, le premier capturant la valeur (in) et l'autre non
    // on voit ici que l'ordre compte, le premier qui correspond est sélectionné
    val r2 = "Z" match {
      case "D" => "Hello D"
      case in: String => "Catched " + in
      case _ => "Fallback"
    }
    r2 shouldBe __

    // Il est possible de mettre plusieurs conditions dans un même case
    val r3 = "C" match {
      case "A" | "B" | "C" => "first"
      case "D" | "F" => "second"
      case _ => "third"
    }
    r3 shouldBe __

    // Maintenant commençons avec les fonctionnalités plus sympa ;)
    // On peut ajouter une condition
    val r4 = "salut" match {
      case s: String if s.length < 3 => "short text"
      case s: String if s.length >= 3 => "long text"
    }
    r4 shouldBe __

    // Le pattern matching supporte les extrateurs, y compris les extracteurs imbriqués
    case class User(name: String, score: Int)
    val r5 = User("toto", 10) match {
      case User(_, score) if score > 10 => "high score"
      case User("toto", score) => s"toto has $score in score"
      case _ => "no match"
    }
    r5 shouldBe __
    case class Talk(title: String, speaker: User, opts: (Boolean, Boolean))
    val r6 = Talk("Scala", User("Luc", 9), (true, false)) match {
      case Talk(title, User(_, score), (true, _)) if score < 10 => s"MATCH for $title"
      case _ => "no match..."
    }
    r6 shouldBe __

    // Enfin, il est possible d'utiliser le pattern matching sur des types
    trait Person
    case class Attendee(name: String) extends Person
    case class Speaker(name: String) extends Person
    val p: Person = Speaker("Marc")
    val r7 = p match {
      case Attendee(name) => s"$name is attendee"
      case Speaker(name) => s"$name is speaker"
    }
    r7 shouldBe __
  }
}
