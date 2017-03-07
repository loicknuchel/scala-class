package models.devoxx.full

import io.circe.generic.auto._
import support.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object DevoxxApi {
  val baseUrl = "http://cfp.devoxx.fr/api"
  val conference = "DevoxxFR2017"

  def getSpeakers(): Future[List[Speaker]] =
    httpGet(s"$baseUrl/conferences/$conference/speakers").flatMap(res => parseJson[List[Speaker]](res).toFuture)

  def getSpeaker(id: SpeakerId): Future[Speaker] =
    httpGet(s"$baseUrl/conferences/$conference/speakers/$id").flatMap(res => parseJson[Speaker](res).toFuture)

  def getTalk(id: TalkId): Future[Talk] =
    httpGet(s"$baseUrl/conferences/$conference/talks/$id").flatMap(res => parseJson[Talk](res).toFuture)
}
