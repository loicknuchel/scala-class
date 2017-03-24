package support

import io.circe._
import org.joda.time.DateTime

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Helpers {

  def parseJson[T](json: String)(implicit decoder: Decoder[T]): Try[T] =
    parser.decode[T](json).toTry

  def formatJson(json: String): String = parser.parse(json) match {
    case Left(_) => json
    case Right(parsed) => parsed.toString()
  }

  implicit val encodeDateTime: Encoder[DateTime] = Encoder.encodeString.contramap[DateTime](_.toString)

  implicit val decodeFoo: Decoder[DateTime] = Decoder.decodeString.emap { str =>
    Try(DateTime.parse(str)) match {
      case Success(date) => Right(date)
      case Failure(err) => Left(s"DateTime: ${err.getMessage}")
    }
  }

  implicit class TryConverter[T](t: Try[T]) {
    def toFuture: Future[T] = t match {
      case Success(value) => Future.successful(value)
      case Failure(err) => Future.failed(err)
    }
  }

  implicit class EitherConverter[T](e: Either[Exception, T]) {
    def toTry: Try[T] = e match {
      case Right(value) => Success(value)
      case Left(err) => Failure(err)
    }

    def toFuture: Future[T] = e match {
      case Right(value) => Future.successful(value)
      case Left(err) => Future.failed(err)
    }
  }

}
