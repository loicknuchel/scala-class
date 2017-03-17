package support

import io.circe.generic.auto._
import models.devoxx.full.DevoxxApi._
import models.devoxx.full._
import support.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object Cache {
  def fillCache(): Future[Unit] = {
    for {
      rooms <- HttpClient.getAndSave(roomsUrl).flatMap(res => parseJson[RoomList](res).toFuture).map(_.rooms)
      schedules <- HttpClient.getAndSave(schedulesUrl).flatMap(res => parseJson[ScheduleList](res).toFuture).map(_.links)
      slots <- getSequence(schedules.map(_.href)).flatMap(parseSequence(parseJson[Schedule])).map(_.flatMap(_.slots))
      talks <- getSequence(slots.flatMap(_.talk).map(_.id).map(talkUrl)).flatMap(parseSequence(parseJson[Talk]))
      talkSpeakers <- getSequence(talks.flatMap(_.speakers).map(_.link.href)).flatMap(parseSequence(parseJson[Talk]))
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

  def getSequence(urls: List[String]): Future[List[String]] = {
    def internal(urlsToProcess: List[String], results: List[String]): Future[List[String]] = {
      if (urlsToProcess.isEmpty) Future.successful(results)
      else HttpClient.getAndSave(urlsToProcess.head).flatMap(res => internal(urlsToProcess.tail, res :: results))
    }

    internal(urls, List()).map(_.reverse)
  }

  def parseSequence[T](p: String => Try[T])(results: List[String]): Future[List[T]] = {
    Future.sequence(results.map(result => p(result).toFuture))
  }

  def main(args: Array[String]): Unit = {
    fillCache().map(_ => {
      println("Cache filled !")
    }).recover {
      case e => println("ERROR: " + e.getMessage)
    }
  }
}
