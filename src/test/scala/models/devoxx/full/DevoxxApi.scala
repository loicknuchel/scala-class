package models.devoxx.full

import io.circe.generic.auto._
import support.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DevoxxApi {
  val baseUrl = "http://cfp.devoxx.fr/api"
  val conference = "DevoxxFR2017"
  val conferenceUrl = s"$baseUrl/conferences/$conference"

  def getSpeakers(): Future[List[Speaker]] =
    httpGet(s"$conferenceUrl/speakers").flatMap(res => parseJson[List[Speaker]](res).toFuture)

  def getSpeaker(id: SpeakerId): Future[Speaker] =
    getSpeakerByUrl(s"$conferenceUrl/speakers/$id")

  def getSpeaker(link: LinkWithName): Future[Speaker] =
    getSpeakerByUrl(link.link.href)

  private def getSpeakerByUrl(url: String): Future[Speaker] =
    httpGet(url).flatMap(res => parseJson[Speaker](res).toFuture)

  def getTalk(id: TalkId): Future[Talk] =
    getTalkByUrl(s"$conferenceUrl/talks/$id")

  def getTalk(link: LinkWithName): Future[Talk] =
    getTalkByUrl(link.link.href)

  private def getTalkByUrl(url: String): Future[Talk] =
    httpGet(url).flatMap(res => parseJson[Talk](res).toFuture)

  def getSchedules(): Future[List[Link]] =
    httpGet(s"$conferenceUrl/schedules/").flatMap(res => parseJson[ScheduleList](res).toFuture).map(_.links)

  def getSchedule(day: String): Future[List[Slot]] =
    getScheduleByUrl(s"$conferenceUrl/schedules/$day")

  def getSchedule(link: LinkWithName): Future[List[Slot]] =
    getScheduleByUrl(link.link.href)

  private def getScheduleByUrl(url: String): Future[List[Slot]] =
    httpGet(url).flatMap(res => parseJson[Schedule](res).toFuture).map(_.slots)

  def getRooms(): Future[List[Room]] =
    httpGet(s"$conferenceUrl/rooms/").flatMap(res => parseJson[RoomList](res).toFuture).map(_.rooms)
}
