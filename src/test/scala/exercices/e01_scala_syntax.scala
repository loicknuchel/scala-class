package exercices

import support.HandsOnSuite

import scala.language.postfixOps

class e01_scala_syntax extends HandsOnSuite {
  /**
    * Avant de commencer :
    *   - installe l'environnement nécessaire, (cf `prérequis.md`)
    *   - lis la présentation d'introduction (cf `docs/slides.html`)
    */

  /**
    * Introduction
    *
    * Scala est un langage basé sur la JVM qui favorise la programmation fonctionnelle aussi bien que l'orienté objet;
    * et ces deux paradigmes fonctionnent très bien ensembles !
    * Il est possible d'écrire du code Scala très proche du code Java mais ce n'est pas vraiment l'idée du siècle...
    * Dans ce handson on verra comment écrire du code Scala qui tire le meilleur parti de la programmation fonctionnelle et objet.
    *
    * De manière simple, si le code est immuable, il y a de bonnes chances pour qu'il ne soit pas si mauvais ;)
    */

  /**
    * Dans ce handson il faudra remplir les trous :
    *   - les `__` sont à remplacer par la valeur manquante
    *   - les `???` sont à remplacer par une implémentation de code
    *
    * Par ailleurs, pour laisser de côté un exercice, il suffit de remplacer `exercice("...")` par `ignore("...")`
    *
    * Enfin, quelques raccourcis utiles pour les utilisateurs d'intellij :
    *   - Alt + Egal : accéder au type de l'expression sélectionnée
    */

  // Solutions de l'exercice (en cas de besoin) : https://github.com/loicknuchel/scala-class/blob/solution/src/test/scala/exercices/e01_scala_syntax.scala

  exercice("Déclarer une variable") {
    // permet de déclarer une variable
    var variable: Int = 5

    // permet de déclarer une valeur (= constante)
    val constant: String = "abc"

    variable = 42 // on peut réassigner une variable
    //constant = "def" // mais pas une valeur
    //variable = true // le type ne doit pas changer

    variable shouldBe 42
    constant shouldBe "abc"
  }


  exercice("Déclarer une fonction") {
    def add(a: Int, b: Int): Int = {
      return a + b;
    }

    add(2, 3) shouldBe 5
  }


  /**
    * Scala est un langage qui se veut concis. Il propose donc :
    *   - de l'inférence de type: le compilateur est capable de déterminer un type sans qu'on ait à le préciser
    *   - return implicit: toute expression retourne une valeur, le mot clé `return` est donc souvent omis
    *   - les `;`, `()` et `.` sont facultatifs
    */
  section("Minimiser le boilerplate") {
    exercice("inférence de type") {
      var name = "Jean" // le type de la variable est inféré en tant que String
      // name = 12 // ne compile pas => type mismatch !
      name shouldBe "Jean"
    }

    // comme tout est expression et retourne une valeur, le mot clé `return` est facultatif
    exercice("le mot clé `return`") {
      def sub(a: Int, b: Int): Int = {
        a - b
      }

      sub(5, 3) shouldBe 2
    }

    // de même, les `{}` servent uniquement à définir un bloc d'exécution
    // lorsqu'on a une seule instruction, elles peuvent être omises aussi
    exercice("les {}") {
      def mult(a: Int, b: Int): Int = a * b

      mult(5, 3) shouldBe 15
    }

    // enfin, l'inférence de type fonctionne aussi pour le type de retour des fonctions
    // mais il est quand même préférable de le fixer "manuellement" pour ne pas avoir de mauvaises surprises
    exercice("type de retour") {
      def div(a: Int, b: Int) = a / b

      //val name: String = div(4, 2) // ne compile pas => type mismatch !
      div(6, 3) shouldBe 2
    }

    // les () et le . étant facultatifs, les syntaxes suivantes sont équivalentes :
    exercice("les ()") {
      val n = 3
      val s1 = n.toString()
      val s2 = n.toString
      val s3 = (n toString)
      s1 shouldBe "3"
      s2 shouldBe "3"
      s3 shouldBe "3"
    }
    // PS: les parenthèses sont souvent omises lorsque la fonction ne prends pas de paramètres (ex: toString)
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
      name shouldBe "Alex"

      // comme tout est expression et retourne une valeur, c'est aussi le cas des if
      // et comme pour les fonctions, les `{}` sont optionnelles dans le cas d'une expression unique
      name = if (num < 4) "Luc" else if (num == 4) "Jean" else "Jules"
      name shouldBe "Jean"
    }

    exercice("for") {
      // passons maintenant à la bonne vieille boucle for...
      var res = 0
      for (i <- 3 to 5) {
        res += i
      }
      res shouldBe 12

      // de la même manière avec une liste
      res = 0
      for (word <- List("table", "chaise")) {
        res += word.length
      }
      res shouldBe 11
    }
    // contrairement aux apparences, à Java et à beaucoup d'autres langages,
    // le for Scala n'est pas le traditionnel `for(initialisation; terminaison; increment){}`
    // mais une structure plus générique du type `for(list of iterator-like statements){}` qui retourne elle aussi une valeur...
    // Mais nous verrons ça plus tard ;)
  }


  section("Astuces pratiques") {
    // parfois on peut avoir des fonctions qui ont "beaucoup" de paramètres du même type (par ex String ou Int)
    // Il est alors facile de se tromper dans l'ordre et assez difficile ensuite trouver le bug
    // Pour cela, Scala donne la possibilité de nommer les paramètres d'une fonction. Leur ordre n'a alors plus d'importance

    exercice("paramètres nommés") {
      def buildReference(project: String, branch: String, commit: String, shortForm: Boolean): String =
        if (shortForm) s"$project~$branch~${commit.substring(0, 7)}" else s"$project~$branch~$commit"

      val ref1 = buildReference("scala-class", "problems", "cd959defd71f36f822dbd741085696b508549763", true)
      val ref2 = buildReference(
        shortForm = false,
        project = "scala-class",
        branch = "solutions",
        commit = "cd959defd71f36f822dbd741085696b508549763"
      )
      ref1 shouldBe "scala-class~problems~cd959de"
      ref2 shouldBe "scala-class~solutions~cd959defd71f36f822dbd741085696b508549763"
    }

    // Il est aussi possible de définir des paramètres par défaut lorsqu'il y a une valeur "évidente"
    // mais attention à ne pas en abuser ;)
    exercice("paramètre par défaut") {
      def push(commit: String, remote: String = "origin", branch: String = "master"): String =
        s"push $commit $remote $branch"

      push("123") shouldBe "push 123 origin master"
      push("123", "gh") shouldBe "push 123 gh master"
      push("123", "gh", "dev") shouldBe "push 123 gh dev"
      push("123", branch = "dev") shouldBe "push 123 origin dev"
    }
  }
}
