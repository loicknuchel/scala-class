package bonus

import support.HandsOnSuite

/**
  * Bienvenu dans les bonus !
  *
  * Les bonus sont des explications facultatives qui permettent d'aller plus loin dans les explications,
  * soit pour montrer des cas plus avancés soit pour expliquer des fonctionnement internes de Scala
  */
class e02_objects extends HandsOnSuite{

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
      def apply(a: Int, b: Int): Int = a  + b
    }
    Add.apply(5, 6) shouldBe __
    Add(7, 1) shouldBe __

    // C'est ce mécanisme qui est utilisé pour les case class afin de pouvoir les créer sans ajouter le mot clé new, apply() jouant le rôle de constructeur
    class NormalClass(val key: String, val value: Int)
    new NormalClass("key", 5).key shouldBe __
    // NormalClass("key", 5).key shouldBe __ // ne compile pas

    class ApplyClass(val key: String, val value: Int)
    object ApplyClass {
      def apply(key: String, value: Int): ApplyClass = new ApplyClass(key, value)
    }
    new ApplyClass("key", 5).key shouldBe __
    ApplyClass("key", 5).key shouldBe __

    case class CaseClass(key: String, value: Int)
    CaseClass("key", 5) == CaseClass.apply("key", 5) shouldBe __
    CaseClass("key", 5) == new CaseClass("key", 5) shouldBe __

    // Pour unapply() c'est assez similaire, elle est sert d'extracteur (c'est l'inverse d'un constructeur, il permet d'extraire les valeurs d'une classe)
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
    val CaseClass(key, value) = caseClass
    key shouldBe __
    value shouldBe __

    // en conclusion, une case class est une classe tout à faire "normale" qui possède certaines méthodes implémentées par défaut (equals, toString, copy...)
    // ainsi qu'un object companion qui lui sert de constructeur / extracteur
    // pas si compliqué que ça ;)
  }
}
