package support

object Config {
  private val folder = "logs"

  object State {
    val file = s"$folder/state.json"
  }

  object Logger {
    val file = s"$folder/events.json"
    val enabled = true
  }

}
