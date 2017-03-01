package part_01_basic

import support.HandsOnSuite

class e01_scala_syntax extends HandsOnSuite {
  /**
    * Prérequis: merci de lire le README.md pour installer l'environnement nécessaire
    */

  /**
    * Introduction
    *
    * Scala est un langage basé sur la JVM qui favorise la programmation fonctionnelle aussi bien que l'orienté objet; et les deux paradigmes fonctionne bien ensembles !
    * Il est possible d'écrire du code Scala très similaire au code Java mais ce n'est pas forcément un bon plan.
    * Dans ce cours on verra comment écrire du code Scala qui tire le meilleur parti de la programmation fonctionnelle et de l'orienté objet.
    * Concrêtement, si le code est immutable, il y a de bonnes chances pour qu'il ne soit pas si mauvais ;)
    */

  /**
    * PS:
    * Scala offre le raccourci `???` qui permet de remplacer n'importe quel bout de code.
    * C'est tout simplement un raccourci pour `throw new NotImplementedError("an implementation is missing")` qui permet de faire compiler du code non terminé
    * Dans ce cours on utilisera un raccourci différent, `__`, qu'il faudra remplacer petit à petit
    */

  exercice("Déclarer une variable") {
    var variable: Int = 5 // permet de déclarer une variable
    val constant: String = "abc" // permet de déclarer une valeur (= constante)

    variable = 42 // on peut réassigner une valeur
    //constant = "def" // mais pas une valeur (immutable FTW !)
    //variable = true // et le type ne doit pas changer

    variable shouldBe __
    constant shouldBe __
  }


  exercice("Déclarer une fonction") {
    def add(a: Int, b: Int): Int = {
      return a + b
    }

    add(2, 3) shouldBe __
  }


  /**
    * Scala est un langage qui se veut concis. Il propose donc :
    *   - de l'inférence de type: le compilateur est capable de déterminer un type sans qu'on ait à le préciser
    *   - return implicit: tout est une expression qui retourne une valeur, le mot clé `return` est donc souvent omis
    */
  exercice("Minimiser le boilerplate") {
    var name = "Jean" // est inféré en tant que String
    // name = 12 // ne compile pas => type mismatch !

    // comme tout est expression et retourne une valeur, le mot clé `return` est facultatif
    def sub(a: Int, b: Int): Int = {
      a - b
    }

    // de même, les `{}` servent uniquement à définir un bloc d'exécution
    // lorsqu'on a une seule instruction, elles peuvent être omises aussi
    def mult(a: Int, b: Int): Int = a * b

    // enfin, l'inférence de type fonctionne aussi pour les fonctions
    // mais il est préférable de fixer "manuellement" le type de retour pour ne pas avoir de mauvaises surprises
    def div(a: Int, b: Int) = a / b
    // name = div(4, 2) // ne compile pas => type mismatch !

    sub(5, 3) shouldBe __
  }


  exercice("Structures de code") {
    val num = 4
    var name = "Pierre"

    if(num > 6) {
      name = "Jacques"
    } else if(num == 6) {
      name = "Claude"
    } else {
      name = "Alex"
    }
    name shouldBe __

    // comme *tout* est expression et retourne une valeur, c'est aussi le cas des if
    // et comme pour les fonctions, les `{}` sont optionnelles dans le cas d'une expression unique
    name = if(num < 4) "Luc" else if(num == 4) "Jean" else "Jules"
    name shouldBe __

    // passons maintenant à la bonne vieille boucle for...
    var res = 0
    for(i <- 1 to 5) {
      res += i
    }
    res shouldBe __

    // de la même manière avec une liste
    res = 0
    for(word <- List("table", "chaise")) {
      res += word.length
    }
    res shouldBe __

    // contrairement aux apparences, à Java et à beaucoup d'autres langages, le for Scala n'est pas le traditionnel `for(initialisation; terminaison; increment){}`
    // mais en fait une structure plus généraliste qui s'approche de `for(list of iterator-like statements){}` qui retourne lui aussi une valeur...
    // Mais nous verrons ça plus tard ;)
  }


  exercice("Astuces pratiques") {
    // parfois on crée des fonctions qui ont "beaucoup" de paramètres du même type (par ex String ou Int)
    // Il est alors facile de se tromper dans l'ordre et assez difficile ensuite trouver le bug
    // Pour cela, Scala donne la possibilité de nommer les paramètres d'une fonction. Leur ordre n'a alors plus d'importance

    def buildReference(project: String, branch: String, commit: String, shortForm: Boolean): String =
      if(shortForm) s"$project~$branch~${commit.substring(0, 7)}" else s"$project~$branch~$commit"

    val ref1 = buildReference("scala-class", "solutions", "cd959defd71f36f822dbd741085696b508549763", true)
    val ref2 = buildReference(
      shortForm = true,
      project = "scala-class",
      branch = "solutions",
      commit = "cd959defd71f36f822dbd741085696b508549763"
    )
    ref1 shouldBe ref2
    ref1 shouldBe __


    // Il est aussi possible de définir des paramètres par défaut lorsqu'il y a une valeur "évidente"
    // mais attention à ne pas en abuser ;)

    def push(commit: String, remote: String = "origin", branch: String = "master"): String =
      s"push $commit $remote $branch"

    push("123") shouldBe __
    push("123", "gh") shouldBe __
    push("123", "gh", "dev") shouldBe __
    push("123", branch = "dev") shouldBe __
  }

  exercice("Mise en pratique") {
    // TODO
  }
}
