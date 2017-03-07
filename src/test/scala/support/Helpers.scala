package support

import io.circe.{Decoder, parser}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Helpers {
  def httpGet(url: String): Future[String] = ??? // TODO

  def parseJson[T](json: String)(implicit decoder: Decoder[T]): Try[T] =
    parser.decode[T](json).toTry

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
