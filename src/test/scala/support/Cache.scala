package support

import io.circe.generic.auto._
import models.devoxx.DevoxxApi._
import models.devoxx._
import support.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Cache {
  def fillCache(): Future[Unit] = {
    // TODO delete files & create new directories
    for {
      rooms <- HttpClient.getAndSave(roomsUrl).flatMap(res => parseJson[RoomList](res).toFuture).map(_.rooms)
      schedules <- HttpClient.getAndSave(schedulesUrl).flatMap(res => parseJson[ScheduleList](res).toFuture).map(_.links)
      slots <- getAndParseSequence(schedules.map(_.href), parseJson[Schedule]).map(_.flatMap(_.slots))
      talks <- getAndParseSequence(slots.flatMap(_.talk).map(_.id).map(talkUrl), parseJson[Talk])
      talkSpeakers <- getAndParseSequence(talks.flatMap(_.speakers).map(_.link.href), parseJson[Speaker])
    // TODO speakerList <- HttpClient.getAndSave(speakersUrl).flatMap(res => parseJson[List[Speaker]](res).toFuture)
    // TODO speakersNotInTalks <- ???
    // TODO talksNotInSlots <- (talkSpeakers ++ speakersNotInTalks).acceptedTalks.filter(not in talks)
    } yield
      println("cache filled with :\n" +
        " - " + rooms.length + " rooms\n" +
        " - " + slots.length + " slots\n" +
        " - " + talkSpeakers.length + " speakers\n" +
        " - " + talks.length + " talks")
  }

  def getAndParseSequence[T](urls: List[String], parse: String => Try[T], results: List[T] = List()): Future[List[T]] = {
    if (urls.isEmpty) Future.successful(results)
    else HttpClient.getAndSave(urls.head).flatMap(res => parse(res) match {
      case Success(value) => getAndParseSequence(urls.tail, parse, value :: results)
      case Failure(err) => {
        println("ERROR: " + err.getMessage)
        println("  url: " + urls.head)
        println("  res: " + res)
        getAndParseSequence(urls.tail, parse, results)
      }
    })
  }

  def main(args: Array[String]): Unit = {
    /*
    TODO :
java.lang.NoClassDefFoundError: support/Cache$$anonfun$fillCache$3$$anonfun$apply$5$$anonfun$apply$19$$anonfun$apply$20
	at support.Cache$$anonfun$fillCache$3$$anonfun$apply$5$$anonfun$apply$19.apply(Cache.scala:18)
     */
    fillCache().map(_ => {
      println("Cache filled !")
    }).recover {
      case e => println("ERROR: " + e.getMessage)
    }
  }
}
