package support

import scala.concurrent.Future
import scala.io.Source
import scala.util.Try
import Helpers._
import models.devoxx.full.DevoxxApi

object HttpClient {
  val localCache = true

  def get(url: String): Future[String] =
    if(localCache) getTry(url).toFuture else ???

  private def urlToPath(url: String): String = (url.replace(DevoxxApi.conferenceUrl, "src/test/resources/devoxx-api")+".json").replace("/.json", ".json")

  private def getTry(url: String): Try[String] = Try(Source.fromFile(urlToPath(url)).getLines().mkString)
}
