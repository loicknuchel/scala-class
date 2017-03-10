package recorder

class AnchorRecorder {
  var records: List[AnchorValue] = Nil

  def record(name: String, line: Int, value: String): Unit = {
    records = AnchorValue(name, line, value) :: records
  }

  def reset() {
    records = Nil
  }
}

case class AnchorValue(name: String, line: Int, value: String) {
  def toMessage(): String = s" anchor line $line | $name => $value"
}
