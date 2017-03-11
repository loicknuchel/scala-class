
import support.HandsOnSuite
import org.scalatest.Matchers._

import scala.None
import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Success, Failure}
import models.devoxx.basic.Talk
import models.devoxx.basic.Slot
import models.devoxx.basic.Room
import models.devoxx.basic.Conference
import org.joda.time.DateTime

class e02_Future extends HandsOnSuite {

  /**
    * TODO
    */


  exercice("Déclarer une Future") {

    val cafe = Future {
      Thread.sleep(500)
      "mon café est pret"
    }

    cafe.onComplete {
      case Success(cafe) => println(cafe)
      case Failure(ex) => println(s"Erreur de préparation : $ex.getMessage")
    }

    val cafePret = Await.result(cafe , 1 second)
    cafePret shouldBe  "mon café est pret"
  }

  exercice("Appliquer une fonction sur Future") {
    // récupèrer le slot du talk DNY-501
    val slot = DevoxxService.getSlotByTalkId("DNY-501")

    // récupèrer la salle du slot 3 en utilisant DevoxxService.getRoomById
    val room = slot.map(slot => DevoxxService.getRoomById(slot.room))

    val roomName = Await.result(room , 1 second)
    roomName.get.name shouldBe  "salle3"
  }

  exercice("Combiner les Futures") {

    // récupèrer le slot du talk DNY-501
    val slot  :Future[Slot] = DevoxxService.getSlotByTalkId("DNY-501")

    // récupèrer les dètails du talk DNY-501
    val talk :Future[Talk] = DevoxxService.getTalk("DNY-501")

    //combiner les deux futures pour en avoir une seul
    val description = slot.zip(talk)
            .map{case (slot : Slot , talk : Talk) => s"le talk ${talk.title} démare à ${slot.from}"}

    val descriptionValue = Await.result(description, 2 second)
    descriptionValue shouldBe  "café lait"
  }

  exercice("Le future des Futures") {
    // récupèrer le slot du talk DNY-501
    val slot :Future[Slot] = DevoxxService.getSlot("DNY-501")

    // récupèrer les dètails du talk DNY-501
    val talk :Future[Talk] = slot.flatMap(slot => DevoxxService.getTalk(slot.talk))

    val talkValue = Await.result(talk , 1 second)

    talkValue.title shouldBe "Le bon testeur il teste..."

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