
import java.io.{File, PrintWriter}

import support.HandsOnSuite
import org.scalatest.Matchers._

import scala.None
import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import models.devoxx.basic.Talk
import models.devoxx.basic.Slot
import models.devoxx.basic.Room
import models.devoxx.basic.Conference
import org.joda.time.DateTime

import scala.io.Source

class e03_Try extends HandsOnSuite {

  /**
    * TODO
    */


  exercice("Déclarer une Future") {

    Try {
      Source.fromFile("Speaker.txt").getLines.toList
    }.map(iter => iter.filter())
    new File("Write.txt").getAbsolutePath shouldBe "titi"
    .foreach { x => print(x) }
  }


  }