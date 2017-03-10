package support

import scala.reflect.macros.blackbox.Context

class HandsOnMacro[C <: Context](val c: C) {
  import c.universe._

  def apply(testName: c.Expr[String])(testFun: c.Expr[Unit])(suite: c.Expr[HandsOnSuite]): c.Expr[Unit] = {
    val texts = getTexts(testFun.tree)
    val content = texts._1
    val start = texts._2
    val end = texts._3

    c.Expr(q"""support.HandsOnSuite.testBody($testName, $suite)($testFun)(new support.TestContext($content, $start, $end))""")
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


object HandsOnMacro {
  def apply(c: Context)(testName: c.Expr[String])(testFun: c.Expr[Unit])(suite: c.Expr[HandsOnSuite]): c.Expr[Unit] = {
    new HandsOnMacro[c.type](c).apply(testName)(testFun)(suite)
  }
}
