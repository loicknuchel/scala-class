package support

case class State(
                  data: Data,
                  logs: List[Log]
                )

object State {
  def load(): State =
    State(
      data = Data.load(),
      logs = Logger.read())
}
