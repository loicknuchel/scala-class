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
  import exercices._
  override def nestedSuites = Vector(
    new e01_scala_syntax,
    new e02_objects,
    new e03_collections
  )
}

class HandsOnBonus extends HandsOn {
  import bonus._
  override def nestedSuites = Vector(
    new e02_objects,
    new e03_collections
  )
}
