import org.scalatest.{Args, Spec, SucceededStatus}
import support.{CustomReporter, CustomStopper}

class HandsOn extends Spec {
  override def run(testName: Option[String], args: Args) = {
    if(!CustomStopper.oneTestFailed)
      super.run(testName, args.copy(reporter = new CustomReporter(args.reporter), stopper = CustomStopper))
    else
      SucceededStatus
  }
}

class HandsOnScala extends HandsOn {
  override def nestedSuites = Vector(
    new exercices.e00_start,
    new exercices.e01_syntaxe,
    new exercices.e02_objet,
    new exercices.e03_collections,
    new exercices.e04_types_fonctionnels,
    new exercices.e05_fonction_dordre_superieur,
    new exercices.e06_currying,
    new exercices.e07_implicit,
    new exercices.end
  )
}

class HandsOnBonus extends HandsOn {
  override def nestedSuites = Vector(
    new exercices.e00_start,
    new exercices.e01_syntaxe,
    new bonus.e01_syntaxe,
    new exercices.e02_objet,
    new bonus.e02_objet,
    new exercices.e03_collections,
    new bonus.e03_collections,
    new exercices.e04_types_fonctionnels,
    new bonus.e04_types_fonctionnels,
    new exercices.e05_fonction_dordre_superieur,
    new exercices.e06_currying,
    new exercices.e07_implicit,
    new exercices.end
  )
}
