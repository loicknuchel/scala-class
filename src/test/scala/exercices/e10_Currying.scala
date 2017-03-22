package exercices

import support.HandsOnSuite

class e10_Currying extends HandsOnSuite {


  /** *
    * Le mécanisme de curryfication permet de décomposer une fonction en applicant de manière partielle ses paramètres
    * Le principe de curryfication peut être fait 'à la volée' ou alors dé explicitement dans la signature de la fonction.
    *
    * Ce mécanisme permet de fixer un contexte d'exécution pour une fonction.
    */

  exercice("Introduction curryffication à la volée") {

    /**
      * Soit la fonction suivante :
      *
      *
      **/

    def addition(a: Int, b: Int, c: Int): Int = a + b + c

    /**
      * Cette fonction prend 3 paramètres. Le principe de curryfication permet d'appliquer la fonction partiellement
      * et donc de transformer cette fonction e 3 foncions à 1 paramètre.
      **/

    val res1 = addition(1, 2, 3)

    res1  shouldEqual 6

    /**
      * équivalent à :
      *
      **/

    val resInter1: (Int, Int) => Int = addition(1, _, _)
    val resInter2: (Int) => Int = resInter1(2, _)
    val resultFinal = resInter2(3)

    resultFinal shouldEqual 6

    /** Dans l'exemple ci-dessus  on s'apercoit que l'on a plus décomposé la fonction à la voléé.
      * Dans cette example, on laisse le choix au déevloppeur sur la manière d'évaluer les paramètres d'une fonction
      * */



  }


}
