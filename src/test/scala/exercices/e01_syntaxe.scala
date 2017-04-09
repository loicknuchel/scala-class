package exercices

import support.HandsOnSuite

import scala.language.postfixOps

class e01_syntaxe extends HandsOnSuite {
  /**
    * Lire le fichier `getting_started.md` avant de démarrer
    */

  /**
    * Introduction
    *
    * Scala est un langage basé sur la JVM qui favorise la programmation fonctionnelle aussi bien que l'orienté objet,
    * et ces deux paradigmes fonctionnent très bien ensemble !
    * Il est possible d'écrire du code Scala très proche du code Java mais ce n'est pas vraiment l'idée du siècle...
    * Dans ce handson on verra comment écrire du code Scala qui tire le meilleur parti de la programmation fonctionnelle et objet.
    *
    * A noter aussi que Scala encourage fortement à écrire du code immuable.
    * Globalement on écrira du code code mutable qu'en dernier recours.
    * De manière simple, si le code est immuable, il y a de bonnes chances pour qu'il ne soit pas si mauvais ;)
    *
    *
    * Dans ce handson il faudra remplir les trous :
    *   - les `__` sont à remplacer par la valeur manquante
    *   - les `???` sont à remplacer par une implémentation de code
    *
    * Par ailleurs, pour laisser de côté un exercice, il suffit de remplacer `exercice("...")` par `ignore("...")`
    *
    * Enfin, quelques raccourcis utiles pour les utilisateurs d'intellij :
    *   - accéder au type de l'expression sélectionnée         : Alt + Egal (Windows/Linux) et Alt + Enter (Mac)
    *   - accéder à la déclaration d'une valeur/fonction/objet : Ctrl + Click (Windows/Linux) et Cmd + Click (Mac)
    */


  exercice("Déclarer une variable") {
    // permet de déclarer une valeur (= constante)
    val constant: String = "abc"
    //constant = "def" // mais pas une valeur (vérifier puis laisser en commentaire)
    constant shouldBe __

    // permet de déclarer une variable (à utiliser au minimum)
    var variable: Int = 5
    variable = 42 // on peut réassigner une variable
    //variable = true // le type ne doit pas changer (vérifier puis laisser en commentaire)
    variable shouldBe __
  }


  exercice("Déclarer une fonction") {
    def add(a: Int, b: Int): Int = {
      return a + b;
    }

    add(2, 3) shouldBe __
  }


  /**
    * Scala est un langage qui se veut concis. Il propose donc :
    *   - de l'inférence de type: le compilateur est capable de déterminer un type sans qu'on ait à le préciser
    *   - return implicit: toute expression retourne une valeur, le mot clé `return` est donc inutile, et déconseillé
    *   - les `;`, `()` et `.` sont facultatifs
    */
  section("Minimiser le boilerplate") {
    exercice("inférence de type") {
      var name = "Jean" // le type de la variable est inféré en tant que String
      // name = 12 // ne compile pas => type mismatch !
      name shouldBe __
    }

    // comme tout est expression (function, if, for, +, ...) et retourne une valeur, le mot clé `return` est facultatif
    exercice("le mot clé `return`") {
      def sub(a: Int, b: Int): Int = {
        a - b
      }

      sub(5, 3) shouldBe __
    }

    // de même, les `{}` servent uniquement à définir un bloc d'exécution
    // lorsqu'on a une seule instruction, elles peuvent être omises aussi
    exercice("les {}") {
      def mult(a: Int, b: Int): Int = a * b

      mult(5, 3) shouldBe __
    }

    // enfin, l'inférence de type fonctionne aussi pour le type de retour des fonctions
    // mais il est quand même préférable de le fixer "manuellement" pour ne pas avoir de mauvaises surprises
    exercice("type de retour") {
      def div(a: Int, b: Int) = a / b

      //val name: String = div(4, 2) // ne compile pas => type mismatch !
      div(6, 3) shouldBe __
    }

    // les () et le . étant facultatifs, les syntaxes suivantes sont équivalentes :
    exercice("les ()") {
      val n = 3
      val s1 = n.toString()
      val s2 = n.toString
      val s3 = (n toString)
      s1 shouldBe __
      s2 shouldBe __
      s3 shouldBe __
    }
    // PS: les parenthèses sont souvent omises lorsque la fonction ne prend pas de paramètres (ex: toString)
    // le reste du temps, mieux vaut conserver les . et () pour plus de clarté
  }


  section("Structures de code") {
    exercice("if") {
      val num = 4
      var name = "Pierre"

      if (num > 6) {
        name = "Jacques"
      } else if (num == 6) {
        name = "Claude"
      } else {
        name = "Alex"
      }
      name shouldBe __

      // comme tout est expression et retourne une valeur, c'est aussi le cas des if
      // et comme pour les fonctions, les `{}` sont optionnelles dans le cas d'une expression unique
      name = if (num < 4) "Luc" else if (num == 4) "Jean" else "Jules"
      name shouldBe __
    }

    exercice("for") {
      // passons maintenant à la bonne vieille boucle for...
      var res = 0
      for (i <- 3 to 5) {
        res += i
      }
      res shouldBe __

      // de la même manière avec une liste
      res = 0
      for (word <- List("table", "chaise")) {
        res += word.length
      }
      res shouldBe __
    }
    // contrairement aux apparences, à Java et à beaucoup d'autres langages,
    // le for Scala n'est pas le traditionnel `for(initialisation; terminaison; increment){}`
    // mais une structure plus générique du type `for(list of iterator-like statements){}` qui retourne elle aussi une valeur...
    // Mais nous verrons ça plus tard ;)
  }


  section("Astuces pratiques") {
    // Le préfix 's' devant une String permet d'utiliser l'interpolation, c'est à dire le fait d'intégrer une valeur.
    // Pour ça il suffit de préfixer la valeur par '$' et éventuellement de l'entourer de '{}' si c'est une expression
    exercice("interpolation") {
      val name = "Loïc"
      val hello = s"Hello $name"
      val count = s"Name has ${name.length} chars"
      val text =
        """
          |Un texte
          |sur plusieurs
          |lignes
        """.stripMargin.trim

      hello shouldBe __
      count shouldBe __
      text.split("\n").length shouldBe __
    }

    // parfois on peut avoir des fonctions qui ont "beaucoup" de paramètres du même type (par ex String ou Int)
    // Il est alors facile de se tromper dans l'ordre et assez difficile de ensuite trouver le bug
    // Pour cela, Scala donne la possibilité de nommer les paramètres d'une fonction. Leur ordre n'a alors plus d'importance
    // Il est aussi possible de définir des paramètres par défaut lorsqu'il y a une valeur "évidente"
    // mais attention à ne pas en abuser ;)
    exercice("paramètres nommés et paramètre par défaut") {
      def buildReference(project: String, branch: String, commit: String, shortForm: Boolean = true): String =
        if (shortForm) s"$project~$branch" else s"$project~$branch~$commit"

      val ref1 = buildReference("scala-class", "problems", "cd959defd71f36f822dbd741085696b508549763")
      val ref2 = buildReference(
        shortForm = false,
        project = "scala-class",
        branch = "solutions",
        commit = "cd959defd71f36f822dbd741085696b508549763"
      )
      ref1 shouldBe __
      ref2 shouldBe __
    }
  }
}
