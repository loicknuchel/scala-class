package support

import io.circe.generic.auto._
import io.circe.syntax._
import support.Helpers.parseJson

import scala.io.Source
import scala.tools.nsc.io.File
import scala.util.Try

case class Data(
                 id: String = java.util.UUID.randomUUID.toString
               )

object Data {
  val config = Config.Data
  val default = Data()

  def load(): Data =
    Try(Source.fromFile(config.file).getLines().mkString).flatMap(parseJson[Data]).getOrElse(save())

  def save(state: Data = default): Data = {
    new java.io.File(config.file).getParentFile.mkdirs()
    File(config.file).writeAll(state.asJson.toString)
    state
  }
}
