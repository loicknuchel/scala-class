package exercices

import models.devoxx.basic.{Conference, Room, Speaker, Talk}
import support.HandsOnSuite

import scala.language.implicitConversions

class e11_implicit extends HandsOnSuite {


  /**
    * Une des fonctionnalité intéressante du langage Scala est l'utilisation d'implicit.
    *
    * Les implicits permettent plusieurs choses :
    *
    *   - Effectuer des opérations dépendante du contexte d'exécution
    *   - D'enrichir un type avec de nouvelles fonctions
    *   - Faire une conversion d'un type A vers un type B implicitement
    *
    * La résolution des imlpicites se base sur le système de type. Lorsque le compilateur se trouve face à un implicit, il va chercher dans les différents
    * scopes un type (class, fonction) qui match celui de l'implicit
    *
    * Une mauvaise compréhension de ce méchanisme ou une mauvaise utilisation peuvent avoir des conséquences importantes
    * sur les temps de compilation de notre programmme.
    * Aussi, cet outil permet de faire des choses assez puissantes, néanmoins il faut rester vigilant et ne pas l'utiliser
    * avec excès.
    */
  exercice("Conversion via un implicit") {
    object OurImplicit {
      implicit def int2Fraction(num: Int): Fraction = Fraction(10, 1)
    }

    case class Fraction(numerator: Int, denominator: Int) {
      def +(other: Fraction): Fraction = Fraction(numerator * other.denominator + other.numerator * denominator, denominator * other.denominator)
    }

    val f1 = Fraction(2, 9)
    val f2 = Fraction(3, 4)

    f1 + f2 shouldEqual __

    import OurImplicit._

    f1 + 10 shouldEqual __
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
      * La méthode initiale ne fait pas partie du type Speaker, néanmoins à l'aide du mécanisme d'implicit appliqué sur une classe
      * cette méthode est maintenant disponible.
      */
    val speaker = Speaker("Azef234", "Martin", "Lapin", "Fr")

    // val initiales = speaker.initiales // does not compile

    import Helpers._

    speaker.initiales shouldEqual __
  }

  object operation {
    implicit def addition(a: Int, b: Int): Int = {
      a + b
    }
    implicit def multiplication(a: Int, b: Int): Int = {
      a * b
    }
  }

  def compute(a: Int, b: Int)(implicit operation: (Int, Int) => Int) = {
    operation(a, b)
  }


  /**
    * La fonction compute(Int,Int):Int attend en paramètre deux entiers et un implicit sous la forme d'une fonction fonction
    * de type (Int, Int) => Int)
    *
    * Pour répondre à la résolution de cet implicit nous déclarons deux fonctions implicites (addition, multiplication) qui match ce type.
    *
    * Pour comprendre le mécanisme de résolution, nous avons deux objects main1 et main2 qui permettent de délimiter un scope de résolution.
    * Si deux types implicites se trouvent dans le même scope de résolution, un confllit sera remonté par le compilateur.
    *
    */
  exercice("Implicit : paramètre 1 (simple)") {
    import operation.addition
    compute(3, 4) shouldEqual __
  }

  exercice("Implicit : paramètre 2 (simple)") {
    import operation.multiplication
    compute(3, 4) shouldEqual __
  }


  /**
    * Dans cet exercice nous allons voir le mécanisme des implicits appliqué au paramètres d'une fonction.
    * Ce mécanisme va nous permettre d'appliquer une paramètre à une fonction selon le contexte d'appel.
    * Cette approche va nous permettre de définir des comportements qui seront résolu automatiquement par le compilateur
    * selon le contexte d'appel.
    *
    * Pour notre exercice, nous souhaitons pourvoir afficher dans le planning de la conférence des informations
    * liées à un Spearker, une room ou encore un talk.
    * Pour chaque entité décrite ci-dessus, les informations à afficher sont différentes. Ceci implique qu'il va falloir gérer spécialement chaque type.
    *
    * Nous allons voir comment les implicites peuvent nous aider.
    */
  exercice("Implicit pattern") {
    //todo à revoir
    object DisplayHelpers {
      /**
        * Ici nous définissons un trait representant la methode permettant d'obtenir une représentation du paramètre sous forme d'une chaîne
        */
      trait Display[A] {
        def show(item: A): String
      }

      /**
        * Ici nous définissons 3 implicits (1 par type) permettant d'opérer la transformation
        */
      implicit object SpeakerDisplay extends Display[Speaker] {
        override def show(item: Speaker): String = s"${item.firstName} ${item.lastName} [${item.lang}]"
      }

      implicit object RoomDisplay extends Display[Room] {
        override def show(item: Room): String = s"${item.name}[${item.capacite}]"
      }

      implicit object TalkDisplay extends Display[Talk] {
        override def show(item: Talk): String = s"${item.title} - ${item.speakers.mkString(",")} ${item.talkType}"
      }
    }

    /**
      * Maintenant nous allons voir comment mettre tout ça en oeuvre
      */
    import DisplayHelpers._

    def display[A](item: A)(implicit display: Display[A]): String = {
      display.show(item)
    }

    val speaker = Speaker("SDfr3", "Harry", "Cover", "Fr")
    val room = Room("AdFgh", "Grand Amphi", Some(200))
    val talk = Talk("mLpo", Conference, "Handson Scala", "découvrir Scala en s'amusant", List("Loic, Walid, Fabrice"))

    /**
      * La résolution de l'implicit se faire sur le type du paramètre passé à la fonction.
      */
    display(speaker) shouldEqual __
    display(talk) shouldEqual __
    display(room) shouldEqual __
  }
}
