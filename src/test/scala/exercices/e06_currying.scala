package exercices

import support.HandsOnSuite

class e06_currying extends HandsOnSuite {


  /**
    * Le mécanisme de curryfication permet de décomposer une fonction en appliquant de manière partielle ses paramètres
    * Le principe de curryfication peut être fait 'à la volée' ou alors explicitement dans la signature de la fonction.
    *
    * Ce mécanisme permet de fixer un contexte d'exécution pour une fonction.
    */

  exercice("Introduction curryffication à la volée") {
    /**
      * Soit la fonction suivante :
      */

    def addition(a: Int, b: Int, c: Int): Int = a + b + c

    /**
      * Cette fonction prends 3 paramètres. Le principe de curryfication permet d'appliquer la fonction partiellement
      * et donc de transformer cette fonction en 3 foncions à 1 paramètre.
      */

    addition(1, 2, 3) shouldBe __

    /**
      * est équivalent à :
      */

    val resInter1: (Int, Int) => Int = addition(1, _, _)
    //(1)
    val resInter2: (Int) => Int = resInter1(2, _)
    //(2)
    val resultFinal = resInter2(3) //(3)

    resultFinal shouldBe __

    /**
      * Dans l'exemple ci-dessus  on s'aperçoit que l'on a pu décomposer la fonction à la volée.
      *
      * (1) : On positionne le premier paramètre avec la valeur 1, c'est ce qu'on appelle une application partielle de la fonction.
      * L'évaluation de cette ligne renverra une nouvelle fonction d'arité 2  -> (Int, Int) => Int
      *
      * (2) : Sur cette ligne, nous utilisons le résultat évalué précédemment. Cette fois-ci, nous positionnons
      * le deuxième paramètre selon le même principe évoqué ci-dessus. Une nouvelle fonction est créée d'arité 1 -> Int => Int
      *
      * (3) : enfin nous fixons le dernier paramètre de la fonction initiale. A ce moment, la fonction (au global) est
      * évaluée et nous obtenons le résultat
      *
      *
      * Dans cet exemple, c'est le développeur utilisateur de la fonction qui décide de décomposer la fonction.
      * Nous allons voir que la currification d'une fonction peut être décrite explicitement.
      */
  }


  exercice("Curryfication explicite") {
    /**
      * La curryfication d'une fonction peut être réalisée explicitement dans la signature de la fonction.
      * On va donc curryfier la fonction addition
      */

    def addition(a: Int)(b: Int)(c: Int): Int = a + b + c

    /**
      * Ici on remarque que l'on passe d'un bloc de 3 paramètres à un 3 blocs de 1 paramètre.
      * On décrit explicitement que cette fonction peut être décomposée.
      *
      * Comment invoquer cette fonction :
      */

    val resInter1: Int => Int => Int = addition(1)
    //(1)
    val resInter2: Int => Int = resInter1(2)
    //(2)
    val resultFinal: Int = resInter2(3) //(3)

    resultFinal shouldBe __

    /**
      * Le principe d'évaluation reste le même que décrit précédemment.
      */
  }


  exercice("Exmple de mise en oeuvre") {
    object BanqueUtils {
      def percent(per: Double)(mnt: Double): Double = (mnt * per) / 100
    }

    /**
      * Nous allons voir maintenant un cas d'utilisation du currying.
      * Pour cela nous allons reprendre le contexte fonctionnel d'une Banque.
      *
      * L'idée ici est de calculer les intérêts acquis sur un compte.
      * Selon le type de compte, le taux d'intérêts varie.
      **/

    /*object Banque {
      def interets(f: Double => Double, cpt: Compte): Compte = {
        Compte(cpt.solde + f(cpt.solde))
      }

      /** Cette case classe représente un compte */
      case class Compte(solde: Double)

      object Compte {
        /** Ici deux fonctions representent des opérations que l'on peut faire sur un compte */
        def debit(cpt: Compte, montant: Double): Compte = Compte(cpt.solde - montant)

        def credit(cpt: Compte, montant: Double): Compte = Compte(cpt.solde + montant)
      }

      /**
        * Maintenant on va pouvoir faire créer un compte et faire des opérations dessus
        **/
      val cpt = Compte(10)

      import Compte._

      debit(cpt, 5) shouldBe __
      credit(cpt, 10) shouldBe __
    }*/
  }
}
