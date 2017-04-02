package exercices

import support.HandsOnSuite

class e05_fonction_dordre_superieur extends HandsOnSuite {

  object BanqueUtils {
    def percent(per: Double)(mnt: Double): Double = (mnt * per) / 100
  }

  /**
    * Dans le chapitre collection nous avons eu l'occasion de voir ce qu'était une fonction d'Ordre Supérieur.
    * Nous allons maintenons les mettre en oeuvre pour nos propres besoins les propriétés de telles fonctions.
    * Pour rappel, une fonction d'ordre supérieur est une fonction qui attends en paramètre une autre fonction
    * et / ou qui renvoie une function en valeur de retour.
    * L'idée est de pouvoir :
    *   - généraliser le comportement d'une fonction
    *   - faire la composition de fonction
    */
  exercice("Avant la Généralisation") {
    /**
      * Pour cet exercice nous allons nous mettre dans la peau d'un banquier.
      * Pour cela, nous allons définir notre modèle métier
      */

    /** Cette case classe représente un compte */
    object Compte {
      /** Ici deux fonctions représentent des opérations que l'on peut faire sur un compte */
      def debit(cpt: Compte, montant: Double): Compte = Compte(cpt.solde - montant)

      def credit(cpt: Compte, montant: Double): Compte = Compte(cpt.solde + montant)
    }
    case class Compte(solde: Double)

    /**
      * Maintenant on va pouvoir créer un compte et faire des opérations dessus
      */

    val cpt = Compte(10)

    import Compte._

    debit(cpt, 5).solde shouldBe __
    credit(cpt, 10).solde shouldBe __
  }
  /**
    * on remarque que si une nouvelle opération devait être rajoutée sur le compte cela passerait
    * par l'ajout d'une fonction dans l'objet companion de la case classe Compte.
    *
    * Entre les opérations de débit et crédit, on remarque que l'on a le même type de fonction Compte, Double => Compte
    * A l'aide de ces infos, on peut donc généraliser à l'aide des propriétés des fonctions d'ordre supérieure
    * de la manière suivante (cf exercice suivant).
    */


  exercice("Après la Généralisation") {
    object Banque {

      /**
        * Nous reprendons notre objet métier Compte
        */
      case class Compte(solde: Double)

      object Compte {
        /**
          * Cette fois-ci nous n'avons plus qu'une méthode dans notre objet companion : opération.
          * On remarque que le premier paramètre est une fonction d'ordre supérieure. Elle va nous permettre
          * d'appliquer différentes opérations sur notre compte.
          */
        def operation(f: (Double, Double) => Double, cpt: Compte, mnt: Double) = Compte(f(cpt.solde, mnt))
      }

    }

    import Banque.Compte
    import Banque.Compte._

    /**
      * Définissons les opérations
      */

    val debit: (Double, Double) => Double = (solde, montant) => solde - montant
    val credit: (Double, Double) => Double = (solde, montant) => solde + montant

    val cpt = Compte(10)

    operation(debit, cpt, 5) shouldBe __
    operation(credit, cpt, 10) shouldBe __

    /**
      * Maintenant que l'on a deux fonctions on va pouvoir les utiliser à différents endroits par exemple pour faire un virement
      **/

    def virement(source: Compte, target: Compte, montant: Double, debit: (Double, Double) => Double, credit: (Double, Double) => Double): (Compte, Compte) = {
      val cptSource = Compte(debit(source.solde, montant))
      val cptTarget = Compte(credit(target.solde, montant))
      (cptSource, cptTarget)
    }

    val cptSource = Compte(100)
    val cptTarget = Compte(50)

    val (source1, target1) = virement(cptSource, cptTarget, 20, debit, credit)

    source1 shouldBe __
    target1 shouldBe __

    /**
      * On souhaite faire des virements entre deux comptes. Les deux opérations (debit / credit ) vont nous être bien utiles.
      * Certains virements peuvent être payants. On va pouvoir créer une nouvelle d'operation de débit qui va calculer un pourcentage
      * du montant du virement pour ensuite le retirer au solde du compte émetteur.
      **/

    def debitPayant: (Double, Double) => Double = (solde, montant) => (solde - montant) - BanqueUtils.percent(0.02)(montant)

    val (source2, target2) = virement(cptSource, cptTarget, 20, debitPayant, credit)

    source2 shouldBe __
    target2 shouldBe __
  }
}
