import sbt.Keys.libraryDependencies

//name := "Wizard"

ThisBuild / version := "SAR-4"

ThisBuild / scalaVersion := "2.13.1"

ThisBuild / trapExit := false

val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.1.1",
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test,
  "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
  "com.typesafe.play" %% "play-json" % "2.8.1"
)

lazy val root = (project in file(".")).settings(
  name := "Wizard",
  libraryDependencies ++= commonDependencies,
  libraryDependencies += "com.google.inject" % "guice" % "4.2.2",
  libraryDependencies +="net.codingwell" %% "scala-guice" % "4.2.6",
).aggregate(ResultTable, CardModule).dependsOn(ResultTable, CardModule) //% "compile->compile;test->test")

lazy val ResultTable = project.settings(
  name := "ResultTable",
  libraryDependencies ++= commonDependencies,
  libraryDependencies += "de.vandermeer" % "asciitable" % "0.3.2"
)

lazy val CardModule = project.settings(
  name :=  "CardModule",
  libraryDependencies ++= commonDependencies
).dependsOn(PlayerModule)