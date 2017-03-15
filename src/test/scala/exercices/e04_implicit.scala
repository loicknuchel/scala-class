package exercices

import models.devoxx.basic.Speaker
import support.HandsOnSuite

/**
  * Created by fsznajderman on 12/03/2017.
  */
class e04_implicit extends HandsOnSuite {


  /**
    * Une des fonctionnalité intéressante du langage Scala est l'utilisation d'implicit.
    *
    * Les implicits permettent plusieurs choses :
    *
    *   - Effectuer des opérations dépendante du contexte d'exécution
    *   - D'enrichir un type avec de nouvelles fonctions
    *   - Faire une conversion d'un type A vers un type B implicitement
    *
    * La résolution des imlpicites se base sur le système de type. 
    *
    * Une mauvaise compréhension de ce méchanisme ou une mauvaise utilisation peuvent avoir des conséquences importantes
    * sur les temps de compilation de notre programmme.
    * Aussi, cet outil permet de faire des choses assez puissantes, néanmoins il faut rester vigilant et ne pas l'utiliser
    * avec excès.
    *
    *
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
    import Helpers._

    val speaker = Speaker("Azef234", "Martin", "Lapin", "Fr")

    speaker.initiales shouldEqual __

  }

  exercice("Implicit : paramètre") {

    /**
      * Ici nous déclarons deux fonctions implicit
      */
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

    object main1 {

      import operation.addition

      compute(3, 4) shouldEqual __


    }

    object main2 {

      import operation.multiplication

      compute(3, 4) shouldEqual __

    }

    //todo à revoir
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


    trait Display[A] {
      def show
    }


  }

}
