package support

import scala.annotation.switch
import scala.collection.mutable.ArrayBuffer

class TestContext(laZysource: => String, val testStartLine: Int, val testEndLine: Int) {
  lazy val source: Array[String] = TestContext.sourceToArray(laZysource)
}

object TestContext {
  final val LF = '\u000A'
  final val FF = '\u000C'
  final val CR = '\u000D'
  final val SU = '\u001A'

  def sourceToArray(source: String): Array[String] = {
    val text = source
    val lineBuf = new ArrayBuffer[String]()
    var charBuf = new ArrayBuffer[Char]()
    var previousChar: Char = 'a'
    for (c <- text.toCharArray) {
      def closeLine() {
        lineBuf.append(charBuf.mkString)
        charBuf = new ArrayBuffer[Char]()
      }

      (c: @switch) match {
        case CR => closeLine()
        case LF => if (previousChar != CR) {
          closeLine()
        }
        case FF | SU => closeLine()
        case _ => charBuf.append(c)
      }

      previousChar = c
    }

    lineBuf.toArray
  }
}
