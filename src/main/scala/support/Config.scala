package support

object Config {
  private val folder = "logs"

  object Data {
    val file = s"$folder/data.json"
  }

  object Logger {
    val file = s"$folder/events.json"
    val enabled = true
  }

}
