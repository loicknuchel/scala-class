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
    new exercices.e01_scala_syntax,
    new exercices.e02_objects,
    new exercices.e03_collections,
    new exercices.e04_types_fonctionnels,
    new exercices.e05_HOF,
    new exercices.e06_Currying,
    new exercices.e07_implicit,
    new exercices.end
  )
}

class HandsOnBonus extends HandsOn {
  override def nestedSuites = Vector(
    new exercices.e00_start,
    new exercices.e01_scala_syntax,
    new exercices.e02_objects,
    new bonus.e02_objects,
    new exercices.e03_collections,
    new bonus.e03_collections,
    new exercices.e04_types_fonctionnels,
    new bonus.e04_types_fonctionnels,
    new exercices.e05_HOF,
    new exercices.e06_Currying,
    new exercices.e07_implicit,
    new exercices.end
  )
}
