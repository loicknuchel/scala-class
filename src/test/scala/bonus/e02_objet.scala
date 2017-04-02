package bonus

import support.HandsOnSuite

class e02_objet extends HandsOnSuite {
  /**
    * Aller plus loin dans la construction d'une classe :
    *   - faire un constructeur privé
    *   - créer d'autres constructeurs
    *   - créer d'autres attributs que ceux dans le constructeur
    *   - dire que le code à l'intérieur de la classes est exécuté à son instantiation
    */


  section("Le secret des case class") {
    /**
      * Avant de parler des `case class`, revenons un peu sur les `object`
      * Comme nous l'avons vu, un object est un singleton qui peut être appelé de n'importe où dans le code.
      */
    exercice("Définition d'un objet") {
      object Op {
        def add(a: Int, b: Int): Int = a + b
      }
      Op.add(4, 2) shouldBe __
    }

    /**
      * Le compilateur Scala possède quelques comportements particuliers faits pour faciliter l'écriture du code.
      * Notamment lorsqu'on utilise directement les parenthèses sur un objet (comme si c'était une fonction),
      * le compilateur cherche la méthode `apply` correspondante et l'appelle avec les arguments.
      *
      * Ainsi, `myVar(5)` est transformé en `myVar.apply(5)`
      */
    exercice("Méthode apply") {
      object Add {
        def apply(a: Int, b: Int): Int = a + b
      }
      Add(7, 1) shouldBe __
      Add.apply(5, 6) shouldBe __
    }

    /**
      * La méthode `apply` est utilisée par l'objet companion des `case class` pour les créer.
      * C'est pourquoi le mot clé `new` n'est pas utilisé.
      */
    exercice("Construire une instance avec apply") {
      class NormalClass(val key: String, val value: Int)
      new NormalClass("key", 5).value shouldBe __
      // NormalClass.apply("key", 5).value shouldBe __ // ne compile pas
      // NormalClass("key", 5).value shouldBe __ // ne compile pas

      class ApplyClass(val key: String, val value: Int)
      object ApplyClass {
        def apply(key: String, value: Int): ApplyClass = new ApplyClass(key, value)
      }
      new ApplyClass("key", 5).value shouldBe __
      ApplyClass.apply("key", 5).value shouldBe __
      ApplyClass("key", 5).value shouldBe __

      case class CaseClass(key: String, value: Int)
      new CaseClass("key", 5).value shouldBe __
      CaseClass.apply("key", 5).value shouldBe __
      CaseClass("key", 5).value shouldBe __
    }
  }


  /**
    * Comme évoqué, un extracteur permet de récupérer les valeurs d'une instance, l'inverse d'un constructeur.
    * Cette opération est faite par un appel masqué à la fonction `unapply` de l'objet en question.
    */
  section("Fonctionnement d'un extracteur") {
    // Créer un extracteur pour une classe "normale"
    exercice("Cas général") {
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

    // Mais très souvent on se sert de l'objet companion pour créer l'extracteur souhaité
    exercice("Cas habituel") {
      class Square(val width: Int)
      object Square {
        def unapply(arg: Square): Option[Int] = Some(arg.width)
      }
      val Square(width) = new Square(3)
      width shouldBe __
    }

    // C'est d'ailleurs comme ça que fonctionnent les `case class`, la méthode `unapply` étant générée dans l'objet companion
    exercice("case class") {
      class NormalClass(val key: String, val value: Int)
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

      case class CaseClass(key: String, value: Int)
      val caseClass = CaseClass("key", 5)
      val CaseClass(cKey, cValue) = caseClass
      cKey shouldBe __
      cValue shouldBe __
    }
    // en conclusion, une case class est une classe tout à faire "normale"
    // qui possède certaines méthodes implémentées par défaut (equals, toString, copy...)
    // ainsi qu'un object companion avec les fonctions apply/unapply qui lui servent de constructeur / extracteur
    // c'est tout simple ;)

    // A noter toutefois, la méthode unapply renvoyant une option de tuple, on est limité à 22 paramètres par les tuples :(
  }
}
