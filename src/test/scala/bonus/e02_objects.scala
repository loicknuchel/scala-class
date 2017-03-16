package bonus

import support.HandsOnSuite

/**
  * Bienvenu dans les bonus !
  *
  * Les bonus sont des explications facultatives qui permettent d'aller plus loin dans les explications,
  * soit pour montrer des cas plus avancés soit pour expliquer des fonctionnement internes de Scala
  */
class e02_objects extends HandsOnSuite {
  /**
    * Class
    *
    * - faire un constructeur privé
    * - créer d'autres constructeurs
    * - créer d'autres attributs que ceux dans le constructeur
    * - dire que le code à l'intérieur de la classes est exécuté à son instantiation
    */

  /**
    * Case class
    */
  exercice("Démystifier les case class") {
    // Avant de parler des `case class`, revenons un peu sur les `object`.
    // Comme nous l'avons vu, un object est un singleton qui peut être appelé de n'importe où dans le code.
    object Op {
      def add(a: Int, b: Int): Int = a + b
    }
    Op.add(4, 2) shouldBe __

    // Un `object` possède deux méthodes spéciales qui peuvent être définies : apply() et unapply()
    // Leur particularité est qu'elle peuvent être appelées sans les nommer directement...
    object Add {
      def apply(a: Int, b: Int): Int = a + b
    }
    Add.apply(5, 6) shouldBe __
    Add(7, 1) shouldBe __

    // C'est ce mécanisme qui est utilisé pour les case class afin de pouvoir les créer sans ajouter le mot clé new, apply() jouant le rôle de constructeur
    class NormalClass(val key: String, val value: Int)
    new NormalClass("key", 5).key shouldBe __
    // NormalClass.apply("key", 5).key shouldBe __ // ne compile pas
    // NormalClass("key", 5).key shouldBe __ // ne compile pas

    class ApplyClass(val key: String, val value: Int)
    object ApplyClass {
      def apply(key: String, value: Int): ApplyClass = new ApplyClass(key, value)
    }
    new ApplyClass("key", 5).key shouldBe __
    ApplyClass.apply("key", 5).key shouldBe __
    ApplyClass("key", 5).key shouldBe __

    case class CaseClass(key: String, value: Int)
    new CaseClass("key", 5).key shouldBe __
    CaseClass.apply("key", 5).key shouldBe __
    CaseClass("key", 5).key shouldBe __

    // Pour unapply() c'est assez similaire, elle est sert d'extracteur
    // c'est l'inverse d'un constructeur, il permet d'extraire les valeurs d'une classe
    val normalClass = new NormalClass("key", 5)
    // val NormalClass(uKey, uValue) = normalClass // ne compile pas

    class UnapplyClass(val key: String, val value: Int)
    object UnapplyClass {
      def unapply(arg: UnapplyClass): Option[(String, Int)] = Some(arg.key, arg.value)
    }
    val unapplyClass = new UnapplyClass("key", 5)
    val UnapplyClass(uKey, uValue) = unapplyClass
    uKey shouldBe __
    uValue shouldBe __

    val caseClass = CaseClass("key", 5)
    val CaseClass(cKey, cValue) = caseClass
    cKey shouldBe __
    cValue shouldBe __

    // en conclusion, une case class est une classe tout à faire "normale" qui possède certaines méthodes implémentées par défaut (equals, toString, copy...)
    // ainsi qu'un object companion avec les fonctions apply/unapply qui lui servent de constructeur / extracteur
    // pas si compliqué que ça ;)
  }

  /**
    * Un extracteur est tout simplement un appel à la fonction `.unapply()` dans l'objet identifié.
    * Cette méthode étant automatiquement générée pour les `case class`, on peut directement les utiliser
    * Mais on peut en créer autant que nécessaire
    */
  section("Fonctionnement d'un extracteur") {
    exercice("Cas général") {
      // On peut créer un extracteur non lié à une classe
      // Dans ce cas, on cherchera la méthode `unapply()` dans l'`object` spécifié
      class User(val name: String, val score: Int)
      class Point(val x: Int, val y: Int)
      object MyExtractor {
        def unapply(arg: User): Option[(String, Int)] = Some((arg.name, arg.score))

        def unapply(arg: Point): Option[(Int, Int)] = Some((arg.x, arg.y))
      }

      val MyExtractor(username, _) = new User("Jean", 10)
      username shouldBe __
      val MyExtractor(x, y) = new Point(1, 2)
      x shouldBe __
    }

    exercice("Cas habituel") {
      // Mais généralement on préfère créer l'extracteur dans l'objet companion de la classe
      class Square(val width: Int)
      object Square {
        def unapply(arg: Square): Option[Int] = Some(arg.width)
      }
      val Square(width) = new Square(3)
      width shouldBe __
    }

    // A noter quand même, la méthode unapply renvoyant une option de tuple, on est limité à 22 paramètres par les tuples :(
  }
}
