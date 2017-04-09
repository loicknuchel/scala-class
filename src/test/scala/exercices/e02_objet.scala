package exercices

import support.HandsOnSuite

class e02_objet extends HandsOnSuite {

  /**
    * Scala est bien connu pour son aspect fonctionnel (et nous le verrons un peu plus loin)
    * mais il est tout aussi capable pour l'orienté objet
    * C'est ce que nous allons voir dans cette partie
    */


  /**
    * Commençons par créer une classe.
    *
    * Les paramètres de la classe seront les paramètres du constructeur et ils seront conservés en tant qu'attribut de la classe :
    *   - s'il n'y a pas de modifieur, l'attribut sera un `private val`
    *   - si le modifieur est val ou var il sera alors public
    *   - sinon on peut entièrement le préciser `public var`, `protected val`...
    */
  exercice("Les classes en Scala") {
    class Point(var x: Int, var y: Int) {
      // `Unit` est un type et un objet Scala qui joue le même rôle que `void` dans d'autres langages
      // On l'utilise quand une fonction/méthode ne renvoit rien
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
  }


  /**
    * Comme évoqué au début, l'immuabilité rends les choses beaucoup plus simple
    * Nous allons donc voir comment nous en servir
    */
  exercice("Une classe immuable") {
    // Créée une classe Point qui corresponde aux tests suivants (les décommenter petit à petit)

    // val p = new Point(1, 2)
    // p.x = 3 // ne doit pas compiler (vérifier puis laisser en commentaire)


    // Implémente la méthode `move` pour la classe Point

    // p.move(2, 1)
    // p.x shouldBe 1 // la classe étant immuable elle ne doit pas pouvoir être modifiée

    // val p1 = p.move(2, 1)
    // p1.x shouldBe 3


    // Implémente la méthode `copy` qui permettra de créer un nouvel objet en modifiant un ou plusieurs paramètres

    // val p2 = p.copy(3, 4)
    // p2.x shouldBe 3
    // p2.y shouldBe 4
    // val p3 = p.copy(x = 5)
    // p3.x shouldBe 5
    // p3.y shouldBe 2
    // val p4 = p1.copy(y = 6)
    // p4.x shouldBe 3
    // p4.y shouldBe 6

    "Finis cet exercice avant de supprimer cette ligne" shouldBe __
  }
  // TIP1: une valeur (val) ne peut pas être modifiée
  // TIP2: si on ne peut pas modifier un objet, alors il faut en créer un nouveau
  // TIP3: pense les paramètres nommés et valeurs par défaut


  /**
    * Scala propose des `case class` qui sont des classes ordinaires avec certains bonus :
    *   - méthodes déjà implémentées :
    *     - equals / hashcode : égalité structurelle : tous les membres doivent être égaux pour que les classes soient égales
    *     - toString          : affiche le nom de la classe et son contenu plutôt que son adresse mémoire
    *     - copy              : permet de créer une nouvelle classe en modifiant quelques attributs
    *   - ne nécessite pas de mot clé `new` à l'instantiation (cf bonus, avec apply/unapply)
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
  }
  // à noter que l'immutabilité obligeant à créer et retourner de nouveaux objets permet d'avoir une fluent API naturellement
  // ex: val result = p.move(3, 4).scale(5).rotate(Point(1, 1), 30)
  // PS: pour aller plus loin avec les case class et comprendre leur fonctionnement, rendez-vous dans les bonus ;)


  /**
    * Le langage Scala n'a pas de `static`.
    * A la place il propose des `object` (nommés objet companions quand ils accompagnent une classe)
    * Ce sont des singletons (instance unique) qui peuvent être appelés depuis n'importe quel endroit du code.
    */
  section("Des 'object' pour remplacer static") {
    exercice("Définition") {
      object Utils {
        def toUpper(str: String): String = str.toUpperCase
      }

      Utils.toUpper("test") shouldBe __
    }

    // on trouve souvent les object associés à des classe pour contenir les fonctions utilitaires non liées à l'instance
    exercice("Object companion") {
      object User {
        def fromFullName(fullName: String): User = {
          val parts = fullName.split(" ")
          User(parts(0), parts(1))
        }
      }
      case class User(firstName: String, lastName: String)

      val user = User.fromFullName("Jean Dupont")
      user shouldBe __
    }

    // une pratique intéressante de programmation fonctionnelle est de coder les méthodes d'une classe comme
    // des fonctions de son objet companion
    // ça permet de réutiliser plus simplement le code et d'avoir une fonction pure facilement testable
    // cette fonction peut soit prendre la classe en paramètre, soit des paramètres plus basiques (et donc plus génériques)
    exercice("minimiser le code des méthodes") {
      object Person {
        def initials(p: Person): String =
          (p.firstName.substring(0, 1) + p.lastName.substring(0, 1)).toLowerCase

        def trigramme(firstName: String, lastName: String): String =
          (firstName.substring(0, 1) + lastName.substring(0, 2)).toLowerCase
      }
      case class Person(firstName: String, lastName: String) {
        def initials(): String = Person.initials(this)

        def trigramme(): String = Person.trigramme(firstName, lastName)
      }

      val person = Person("Jeanne", "Michu")
      person.initials shouldBe __
      person.trigramme shouldBe __
      Person.trigramme("Jean-Claude", "Convenant") shouldBe __
    }
  }


  /**
    * Les traits Scala sont très similaires aux interfaces de Java 8.
    * Ils peuvent contenir des variables, des valeurs et/ou des méthodes. Celles-ci peuvent être abstraites ou concrètes.
    * De plus, il est aussi possible d'hériter de plusieurs traits (comme une interface)
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
    * Dans ce cas, on peut créer un objet spécifique avec ces différents résultats
    * ou utiliser un objet générique qui peut contenir plusieurs valeurs.
    * Par exemple en Java, on crée souvent un objet Pair<A, B> (ex: https://github.com/search?q=filename%3APair.java)
    *
    * En Scala, on a les tuples qui sont des case class qui peuvent contenir un nombre de valeurs hétérogènes fixe.
    * Les tuples peuvent contenir entre 1 à 22 valeures.
    */
  exercice("Les tuples") {
    val pair: Tuple2[Int, String] = (1, "test")
    pair._1 shouldBe __ // accéder à la première valeur du tuple
    pair._2 shouldBe __

    // n'importe quelle valeur peut être dans un tuple
    val multi = (3, 2.0, "1", (n: Int) => n + 1)
    multi._3 shouldBe __
    multi._4(multi._1) shouldBe __
  }


  /**
    * Un extracteur est l'inverse d'un constructeur, il permet d'extraire les valeurs d'un objet.
    * Scala propose des extracteurs par défaut pour les `tuples` et les `case class`
    */
  exercice("Extracteurs") {
    val pair = (2, "name")
    val (value, name) = pair // On extrait les valeurs du tuple dans deux variables indépendantes
    value shouldBe __
    name shouldBe __

    // De même pour une `case class`
    case class Circle(x: Int, y: Int, r: Int)
    val Circle(_, _, r) = Circle(3, 3, 5) // les _ sont utilisés pour les valeurs que l'on ne souhaite pas affecter
    r shouldBe __
  }
  // PS: voir les bonus pour plus de détails sur le fonctionnement des extracteurs...


  /**
    * Le pattern matching est un mécanisme similaire au `switch` dans d'autres langages mais en étant bien plus puissant.
    * Par ailleurs, comme en Scala toute expression retourne une valeur, c'est aussi le cas du pattern matching
    */
  section("Le pattern matching") {
    // L'ordre des `case` compte, le premier qui correspond est sélectionné
    exercice("cas général") {
      val result = "Z" match {
        case "A" => "it's A"
        case "B" => "B wins"
        case in: String => "Catched " + in
        case _ => "Fallback"
      }
      result shouldBe __
    }

    // Il est possible de mettre plusieurs conditions dans un même case
    exercice("grouper les cas") {
      val result = "C" match {
        case "A" | "B" | "C" => "first"
        case "D" | "F" => "second"
        case _ => "third"
      }
      result shouldBe __
    }

    // Maintenant commençons avec les fonctionnalités plus sympa ;)
    // On peut ajouter une condition
    exercice("ajouter une condition") {
      val result = "salut" match {
        case s: String if s.length < 3 => "short text"
        case s: String if s.length >= 3 => "long text"
      }
      result shouldBe __
    }

    // Le pattern matching supporte les extrateurs, y compris les extracteurs imbriqués
    exercice("extraire des valeurs") {
      case class User(name: String, score: Int)
      val r1 = User("toto", 10) match {
        case User(_, score) if score > 10 => "high score"
        case User("toto", score) => s"toto has $score in score"
        case _ => "no match"
      }
      r1 shouldBe __

      case class Talk(title: String, speaker: User, opts: (Boolean, Boolean))
      val r2 = Talk("Scala", User("Luc", 9), (true, false)) match {
        case Talk(title, User(_, score), (true, _)) if score < 10 => s"MATCH for $title"
        case _ => "no match..."
      }
      r2 shouldBe __
    }

    // Enfin, il est possible d'utiliser le pattern matching sur des types
    exercice("différentier des types") {
      trait Person
      case class Attendee(name: String) extends Person
      case class Speaker(name: String) extends Person
      val p: Person = Speaker("Marc")
      val result = p match {
        case Attendee(name) => s"$name is attendee"
        case Speaker(name) => s"$name is speaker"
      }
      result shouldBe __
    }

    exercice("extraire une expression régulière") {
      val regex = """Point\((\d+),(\d+)\)""".r
      val result = "Point(1,3)" match {
        case regex(x, y) => s"Match $x, $y"
        case _ => "No match"
      }
      result shouldBe __
    }
  }
}
