
import support.HandsOnSuite
import org.scalatest.Matchers._

import scala.None
import scala.concurrent.Future
import scala.concurrent.forkjoin._

class e01_Future extends HandsOnSuite {

  /**
    * TODO
    */


  exercice("Déclarer une Future") {

    val cafe = Future {
      Thread.sleep(1000)
      "mon café est pret"
    }

    cafe.onComplete {
      case Success(cafe) => println(cafe)
      case Failure(ex) => println(s"Erreur de préparation : $ex.getMessage")
    }

    val cafePret = Await.result(cafe)
    cafePret shouldBe  "mon café est pret"
  }

  exercice("Appliquer une fonction sur Future") {
    val cafe = Future {
      Thread.sleep(1000)
      "mon café est pret"
    }

    //Aplliquer une fonction sur une future
    cafe.map(cafe => s"$cafe avec du sucre")

    val cafePret = Await.result(cafe)
    cafePret shouldBe  "mon café est pret avec du sucre"
  }

  exercice("Combiner les Futures") {
    val cafe = Future {
      Thread.sleep(1000)
      "café"
    }
    val lait = Future {
      Thread.sleep(2000)
      "lait"
    }

    val cafeAuLait = cafe.zip(lait)
                          .map((cafe, lait) => s"$cafe $lait")

    val cafePret = Await.result(cafeAuLait)
    cafePret shouldBe  "café lait"
  }
}