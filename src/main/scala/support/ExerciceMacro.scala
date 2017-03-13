package support

import scala.reflect.macros.blackbox.Context

class ExerciceMacro[C <: Context](val c: C) {
  import c.universe._

  def apply(testName: c.Expr[String])(testFun: c.Expr[Unit])(suite: c.Expr[HandsOnSuite]): c.Expr[Unit] = {
    val code = testFun.tree.pos.source.content.mkString
    val (start, end) = testFun.tree match {
      case Block(xs, y) => (testFun.tree.pos.line, y.pos.line)
      case _ => (testFun.tree.pos.line, testFun.tree.pos.line)
    }
    c.Expr(q"""$suite.test($testName)($testFun)(new support.TestContext($code, $start, $end))""")
  }
}

object ExerciceMacro {
  def apply(c: Context)(testName: c.Expr[String])(testFun: c.Expr[Unit])(suite: c.Expr[HandsOnSuite]): c.Expr[Unit] = {
    new ExerciceMacro[c.type](c).apply(testName)(testFun)(suite)
  }
}
