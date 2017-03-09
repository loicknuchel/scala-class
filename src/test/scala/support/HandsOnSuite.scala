package support

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{FunSpec, Matchers}

trait HandsOnSuite extends FunSpec with Matchers with ScalaFutures {
  lazy val __ : Any = null
  val exercice = it
  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))
}
