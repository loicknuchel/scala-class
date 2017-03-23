import bonus.{e04_Try, e05_Either}
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
    new exercices.e01_scala_syntax,
    new exercices.e02_objects,
    new exercices.e03_collections,
    new exercices.e04_Option,
    new exercices.e05_Future,
    new exercices.e06_Monad,
    new exercices.e07_HOF,
    new exercices.e08_Currying,
    new exercices.e09_implicit
  )
}

class HandsOnBonus extends HandsOn {
  override def nestedSuites = Vector(
    new exercices.e01_scala_syntax,
    new exercices.e02_objects,
    new bonus.e02_objects,
    new exercices.e03_collections,
    new bonus.e03_collections,
    new exercices.e04_Option,
    new bonus.e04_Try,
    new exercices.e05_Future,
    new bonus.e05_Either,
    new exercices.e06_Monad,
    new exercices.e07_HOF,
    new exercices.e08_Currying,
    new exercices.e09_implicit
  )
}
