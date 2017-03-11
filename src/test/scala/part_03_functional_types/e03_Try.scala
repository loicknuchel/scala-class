
import java.io.{File, PrintWriter}

import support.HandsOnSuite
import org.scalatest.Matchers._

import scala.None
import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}
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

    try {
      val writer = new PrintWriter(new File("Write.txt"))

      writer.write("Hello Developer, Welcome to Scala Programming.")
      writer.close()
    }
    print(new File("Write.txt").getAbsolutePath)
    Source.fromFile("Write.txt").foreach { x => print(x) }
  }

  object DevoxxService {

    def getSlotByTalkId(talkId :String) : Future[Slot]= {
      Future{
        // simuler la latence du réseau
        Thread.sleep(100)
        Slot(id = "slot_3", room ="3",talk = talkId ,from =new DateTime(), to = new DateTime() )
      }
    }

    def getSlot(id :String) : Future[Slot]= {
      Future{
        // simuler la latence du réseau
        Thread.sleep(100)
        Slot(id = id, room ="3",talk = "DNY-501" ,from =new DateTime(), to = new DateTime() )
      }
    }

    def getTalk(id: String): Future[Talk] = {
      Future{
        // simuler la latence du réseau
        Thread.sleep(100)
        Talk(id, Conference, "Le bon testeur il teste...", "Pourquoi proposer une nouvelle conférence sur les tests ?", List("6cbb41adbc049f923c4327ed3642f208faf4e03f"))
      }
    }

    def getRoomById(id: String): Option[Room] = {
      if(id.toInt < 8 )
        Some(Room(id,s"salle$id", Some(id.toInt * 10)))
      else
        None
    }

  }

}