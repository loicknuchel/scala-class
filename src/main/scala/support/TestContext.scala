package support

import scala.annotation.switch
import scala.collection.mutable.ArrayBuffer

class TestContext(code: => String, val startLine: Int, val endLine: Int) {
  lazy val lines: List[(Int, String)] = TestContext.getLines(code)
}

object TestContext {
  final val CR = '\u000D'
  final val LF = '\u000A'
  final val FF = '\u000C'
  final val SU = '\u001A'

  def getLines(code: String): List[(Int, String)] = {
    val lineBuf = new ArrayBuffer[String]()
    var charBuf = new ArrayBuffer[Char]()
    var previousChar: Char = 'a'
    def closeLine() {
      lineBuf.append(charBuf.mkString)
      charBuf = new ArrayBuffer[Char]()
    }

    for (c <- code.toCharArray) {
      (c: @switch) match {
        case CR => closeLine()
        case LF => if (previousChar != CR) closeLine()
        case FF | SU => closeLine()
        case _ => charBuf.append(c)
      }
      previousChar = c
    }

    lineBuf.toList.zipWithIndex.map(t => (t._2 + 1, t._1))
  }
}
