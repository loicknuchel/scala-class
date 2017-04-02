package exercices

import models.devoxx._
import support.HandsOnSuite

import scala.language.implicitConversions

class e07_implicit extends HandsOnSuite {
  /**
    * Une des fonctionnalité intéressante du langage Scala est l'utilisation d'implicites.
    *
    * Les implicites permettent plusieurs choses :
    *
    *   - Effectuer des opérations dépendante du contexte d'exécution
    *   - D'enrichir un type avec de nouvelles fonctions
    *   - Faire une conversion d'un type A vers un type B implicitement
    *
    * La résolution des implicites se base sur le système de type. Lorsque le compilateur se trouve face à un implicite, il va chercher dans les différents
    * scopes un type (class, fonction) qui correspond à celui attendu pour l'implicite.
    *
    * Une mauvaise compréhension de ce méchanisme ou une mauvaise utilisation peuvent avoir des conséquences importantes
    * sur les temps de compilation de notre programmme.
    * Aussi, cet outil permet de faire des choses assez puissantes, néanmoins il faut rester vigilant et l'utiliser sans excès.
    */
  exercice("Conversion via un implicite") {
    object OurImplicit {
      implicit def int2Fraction(num: Int): Fraction = Fraction(num, 1)
    }

    case class Fraction(numerator: Int, denominator: Int) {
      def +(other: Fraction): Fraction = Fraction(numerator * other.denominator + other.numerator * denominator, denominator * other.denominator)
    }

    val f1 = Fraction(2, 9)
    val f2 = Fraction(3, 4)

    f1 + f2 shouldBe __

    import OurImplicit._

    f1 + 10 shouldBe __
  }


  exercice("Implicit : Enrichissment de type") {
    object Helpers {

      /**
        * Ici nous allons enrichir un type existant avec de nouvelles fonctionnalités. Ceci sans mécanisme d'héritage ni modification du type original.
        * L'objectif est de rajouter au type Speaker un fonction permettant d'extraire ses initiales
        */
      implicit class SpeakerTools(speaker: Speaker) {
        def initiales: String = s"${speaker.firstName.toUpperCase.head}. ${speaker.lastName.toUpperCase.head}."
      }

    }

    /**
      * La méthode initiale ne fait pas partie du type Speaker, néanmoins à l'aide du mécanisme d'implicite appliqué sur une classe
      * cette méthode est maintenant disponible.
      */
    val speaker = Speaker("Azef234", "Martin", "Lapin")

    // val initiales = speaker.initiales // ne compile pas

    import Helpers._

    speaker.initiales shouldBe __
  }


  object operation {
    implicit def addition(a: Int, b: Int): Int = a + b

    implicit def multiplication(a: Int, b: Int): Int = a * b
  }

  def compute(a: Int, b: Int)(implicit operation: (Int, Int) => Int): Int = operation(a, b)

  /**
    * La fonction `compute(Int,Int): Int` attend en paramètre deux entiers et un implicite sous la forme d'une fonction
    * de type (Int, Int) => Int
    *
    * Pour répondre à la résolution de cet implicite nous déclarons deux fonctions implicites (addition, multiplication) qui correspondent à ce type.
    *
    * Pour comprendre le mécanisme de résolution, nous avons deux objects main1 et main2 qui permettent de délimiter un scope de résolution.
    * Si deux types implicites se trouvent dans le même scope de résolution, un confllit sera remonté par le compilateur.
    */
  exercice("Implicit : paramètre 1 (simple)") {
    import operation.addition
    compute(3, 4) shouldBe __
  }

  exercice("Implicit : paramètre 2 (simple)") {
    import operation.multiplication
    compute(3, 4) shouldBe __
  }


  /**
    * Dans cet exercice nous allons voir le mécanisme des implicites appliqué au paramètres d'une fonction.
    * Ce mécanisme va nous permettre d'appliquer un paramètre à une fonction selon le contexte d'appel.
    * Cette approche va nous permettre de définir des comportements qui seront résolu automatiquement par le compilateur
    * selon le contexte d'appel.
    *
    * Pour notre exercice, nous souhaitons pouvoir afficher dans le planning de la conférence des informations
    * liées à un Speaker, une room ou encore un talk.
    * Pour chaque entité décrite ci-dessus, les informations à afficher sont différentes. Ceci implique qu'il va falloir gérer spécialement chaque type.
    *
    * Nous allons voir comment les implicites peuvent nous aider.
    */
  exercice("Implicit pattern") {
    //todo à revoir
    object DisplayHelpers {
      /**
        * Définissons un trait représentant la méthode permettant d'obtenir une représentation du paramètre sous forme d'une chaîne
        */
      trait Display[A] {
        def show(item: A): String
      }

      /**
        * Définissons 3 implicites (1 par type) permettant d'opérer la transformation
        */
      implicit object SpeakerDisplay extends Display[Speaker] {
        override def show(item: Speaker): String = s"${item.firstName} ${item.lastName} [${item.lang}]"
      }

      implicit object RoomDisplay extends Display[Room] {
        override def show(item: Room): String = s"${item.name}[${item.capacity}]"
      }

      implicit object TalkDisplay extends Display[Talk] {
        override def show(item: Talk): String = s"${item.title} - ${item.speakers.mkString(",")} ${item.talkType}"
      }
    }

    /**
      * Maintenant nous allons voir comment mettre tout ça en oeuvre
      */
    import DisplayHelpers._

    def display[A](item: A)(implicit display: Display[A]): String =
      display.show(item)

    val speaker = Speaker("SDfr3", "Harry", "Cover")
    val room = Room("AdFgh", "Grand Amphi", "theatre", 200, None)
    val talk = Talk("mLpo", "Tools-in-Action", "java", "Handson", "fr", "découvrir Scala en s'amusant", "summary", "summaryAsHtml", List(LinkWithName("Loic", Link("Loic", "href", "rel")), LinkWithName("Walid", Link("Walid", "href", "rel")), LinkWithName("Fabrice", Link("Fabrice", "href", "rel"))))

    /**
      * La résolution de l'implicite se fait sur le type du paramètre passé à la fonction.
      */
    display(speaker) shouldBe __
    display(talk) shouldBe __
    display(room) shouldBe __
  }

  /**
    * Pour aller plus loin avec les implicites, voici un article qui détaille leur fonctionnement et utilisation :
    *
    *   http://www.lihaoyi.com/post/ImplicitDesignPatternsinScala.html
    *
    *
    * Et voici quelques exemples réels de leur utilisation :
    *
    * - Paramètre implicite : Play framework pour gérer l'encodage
    *     code: https://github.com/playframework/playframework/blob/38abd1ca6d17237950c82b1483057c5c39929cb4/framework/src/play/src/main/scala/play/utils/PlayIO.scala#L53
    *     codec doc: https://www.scala-lang.org/api/current/scala/io/Codec.html
    *     L'objet codec permet de spéifier l'encodage et est automatiquement passé en paramètre via les implicites
    *
    * - Classe implicite : Play framework pour ajouter un timeout sur un Future
    *     code: https://github.com/playframework/playframework/blob/afcda67c5cada258a662f77c58f57ead3ecfbac8/framework/src/play/src/main/scala/play/api/libs/concurrent/Timeout.scala#L90
    */
}
