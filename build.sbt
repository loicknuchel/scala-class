name := "scala-class"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= List(
  "joda-time" % "joda-time" % "2.9.7",
  "io.circe" %% "circe-core" % "0.7.0",
  "io.circe" %% "circe-generic" % "0.7.0",
  "io.circe" %% "circe-parser" % "0.7.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
