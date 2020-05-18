import sbt.Keys.libraryDependencies

name := "Wizard"

version := "0.13"

scalaVersion := "2.13.2"

trapExit := false

val dependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.1.1",
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test,

  "org.scala-lang.modules" %% "scala-swing" % "2.1.1",

  "com.google.inject" % "guice" % "4.2.2",
  "net.codingwell" %% "scala-guice" % "4.2.6",


  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",

  "com.typesafe.play" %% "play-json" % "2.8.1",

  // https://mvnrepository.com/artifact/de.vandermeer/asciitable
  "de.vandermeer" % "asciitable" % "0.3.2",
)

libraryDependencies ++= dependencies

