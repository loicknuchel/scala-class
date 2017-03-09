package support.scalatest

import scala.reflect.macros.blackbox.Context

class RecorderMacro[C <: Context](val c: C) {

  import c.universe._

  def apply(testName: c.Expr[String])
           (testFun: c.Expr[Unit])
           (suite: c.Expr[MyFunSuite], anchorRecorder: c.Expr[AnchorRecorder]): c.Expr[Unit] = {

    val texts = getTexts(testFun.tree)
    val content = texts._1
    val start = texts._2
    val end = texts._3

    c.Expr(
      q"""recorder.MyFunSuite.testBody($testName, $suite, $anchorRecorder)($testFun)(new recorder.TestContext(
      $content, $start, $end))
     """)
  }

  def getTexts(recording: Tree): (String, Int, Int) = {
    def lines(rec: Tree): (Int, Int) = {
      rec match {
        case Block(xs, y) => (rec.pos.line, y.pos.line)
        case _ => (rec.pos.line, rec.pos.line)
      }
    }

    val (lstart, lend) = lines(recording)
    val source = recording.pos.source
    val sourceContent: String = source.content.mkString
    (sourceContent, lstart, lend)
  }
}


object RecorderMacro {
  def apply(c: Context)(testName: c.Expr[String])
           (testFun: c.Expr[Unit])
           (suite: c.Expr[MyFunSuite], anchorRecorder: c.Expr[AnchorRecorder]): c.Expr[Unit] = {
    new RecorderMacro[c.type](c).apply(testName)(testFun)(suite, anchorRecorder)
  }

  def anchor[T: c.WeakTypeTag](c: Context)(a: c.Expr[T]): c.Expr[Unit] = {
    import c.universe._
    val aCode = q"${show(a.tree)}"
    val line = q"${a.tree.pos.line}"
    val resultExp = q"${reify(a.splice.toString())}"
    c.Expr[Unit](q"anchorRecorder.record($aCode, $line, $resultExp)")
  }
}
