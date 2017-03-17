package models.devoxx

import io.circe.generic.auto._
import support.Helpers._
import support.HttpClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DevoxxApi {
  private implicit val useCache = true
  val baseUrl = "http://cfp.devoxx.fr/api"
  val conference = "DevoxxFR2017"
  val conferenceUrl = s"$baseUrl/conferences/$conference"
  val roomsUrl = s"$conferenceUrl/rooms/"
  val schedulesUrl = s"$conferenceUrl/schedules/"
  def scheduleUrl(day: String) = s"$conferenceUrl/schedules/$day"
  val speakersUrl = s"$conferenceUrl/speakers"
  def speakerUrl(id: SpeakerId) = s"$conferenceUrl/speakers/$id"
  def talkUrl(id: TalkId) = s"$conferenceUrl/talks/$id"

  def getRooms(): Future[List[Room]] =
    HttpClient.get(roomsUrl).flatMap(res => parseJson[RoomList](res).toFuture).map(_.rooms)

  def getSpeakers(): Future[List[Speaker]] =
    HttpClient.get(speakersUrl).flatMap(res => parseJson[List[Speaker]](res).toFuture)

  def getSpeaker(id: SpeakerId): Future[Speaker] =
    getSpeakerByUrl(speakerUrl(id))

  def getSpeaker(link: LinkWithName): Future[Speaker] =
    getSpeakerByUrl(link.link.href)

  private def getSpeakerByUrl(url: String): Future[Speaker] =
    HttpClient.get(url).flatMap(res => parseJson[Speaker](res).toFuture)

  def getTalk(id: TalkId): Future[Talk] =
    getTalkByUrl(talkUrl(id))

  def getTalk(link: LinkWithName): Future[Talk] =
    getTalkByUrl(link.link.href)

  private def getTalkByUrl(url: String): Future[Talk] =
    HttpClient.get(url).flatMap(res => parseJson[Talk](res).toFuture)

  def getSchedules(): Future[List[Link]] =
    HttpClient.get(schedulesUrl).flatMap(res => parseJson[ScheduleList](res).toFuture).map(_.links)

  def getSchedule(day: String): Future[List[Slot]] =
    getScheduleByUrl(scheduleUrl(day))

  def getSchedule(link: Link): Future[List[Slot]] =
    getScheduleByUrl(link.href)

  private def getScheduleByUrl(url: String): Future[List[Slot]] =
    HttpClient.get(url).flatMap(res => parseJson[Schedule](res).toFuture).map(_.slots)

  def getModel(): Future[(List[Speaker], List[Talk], List[Room], List[Slot])] = {
    for {
      rooms <- getRooms()
      schedules <- getSchedules()
      slots <- Future.sequence(schedules.map(getSchedule)).map(_.flatten)
      talks <- Future.sequence(slots.flatMap(_.talk).map(_.id).distinct.map(getTalk))
      speakers <- Future.sequence(talks.flatMap(_.speakers).distinct.map(getSpeaker))
    } yield (speakers, talks, rooms, slots)
  }
}
