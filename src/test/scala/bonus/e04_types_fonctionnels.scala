package bonus

import support.HandsOnSuite

class e04_types_fonctionnels extends HandsOnSuite {
  /**
    * La vraie définition d'une Monade est :
    * ```
    * trait Monad[+A] {
    *   def apply(a: A): Monad[A]
    *   // si ça respecte les 3 lois monadiques : element neutre a droite, element neutre a gauche, associativité
    *   def flatMap[B](f: A => Monad[B]): Monad[B]
    * }
    * ```
    */

  /**
    * Détail for-compréhension :
    *   - décomposition map / flatMap / withFilter
    */

  /**
    * Créer une Monade Validation
    * Utiliser la Validation dans une for-comprehension
    */
}
