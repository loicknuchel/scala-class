package support

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import models.devoxx.DevoxxApi
import support.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Try

object HttpClient {
  private implicit val system = ActorSystem()
  private implicit val materializer = ActorMaterializer()

  def get(url: String)(implicit useCache: Boolean): Future[String] =
    if (useCache) localGet(url) else httpGet(url)

  def getAndSave(url: String): Future[String] =
    httpGet(url).flatMap(res => localSave(url, formatJson(res)))

  private def httpGet(url: String): Future[String] = {
    println("GET " + url)
    Http().singleRequest(HttpRequest(uri = url)).flatMap(_.entity.toStrict(300.millis).map(_.data.utf8String))
  }

  private def urlToPath(url: String): String =
    (url.replace(DevoxxApi.conferenceUrl, "src/test/resources/devoxx-api") + ".json").replace("/.json", ".json")

  private def localGet(url: String): Future[String] =
    Try(Source.fromFile(urlToPath(url)).getLines().mkString).toFuture

  private def localSave(url: String, content: String): Future[String] = Try {
    val file = new File(urlToPath(url))
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(content)
    bw.close()
    content
  }.toFuture
}

