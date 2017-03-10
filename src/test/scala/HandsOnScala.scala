import org.scalatest.{Args, Spec, SucceededStatus}
import support.scalatest.{CustomStopper, ReportToTheStopper}

class HandsOn extends Spec {
  override def run(testName: Option[String], args: Args) = {
    if(!CustomStopper.oneTestFailed)
      super.run(testName, args.copy(reporter = new ReportToTheStopper(args.reporter), stopper = CustomStopper))
    else
      SucceededStatus
  }
}

class HandsOnScala extends HandsOn {
  override def nestedSuites = Vector(
    new exercices.e01_scala_syntax,
    new exercices.e02_objects,
    new exercices.e03_collections
  )
}

class HandsOnBonus extends HandsOn {
  override def nestedSuites = Vector(
    new exercices.e01_scala_syntax,
    new exercices.e02_objects,
    new bonus.e02_objects,
    new exercices.e03_collections,
    new bonus.e03_collections
  )
}
