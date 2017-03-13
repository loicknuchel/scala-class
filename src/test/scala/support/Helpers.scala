package support

import io.circe.{Decoder, parser}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Helpers {

  def parseJson[T](json: String)(implicit decoder: Decoder[T]): Try[T] =
    parser.decode[T](json).toTry

  def formatJson(json: String): String = parser.parse(json) match {
    case Left(_) => json
    case Right(parsed) => parsed.toString()
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
