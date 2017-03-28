package support

import io.circe.generic.auto._
import io.circe.syntax._
import support.Helpers.parseJson

import scala.io.Source
import scala.tools.nsc.io.File
import scala.util.Try

case class State(
                  id: String = java.util.UUID.randomUUID.toString
                )

object State {
  val config = Config.State
  val default = State()

  def load(): State =
    Try(Source.fromFile(config.file).getLines().mkString).flatMap(parseJson[State]).getOrElse(save())

  def save(state: State = default): State = {
    new java.io.File(config.file).getParentFile.mkdirs()
    File(config.file).writeAll(state.asJson.toString)
    state
  }
}
