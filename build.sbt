import sbt.Keys.libraryDependencies

//name := "Wizard"

ThisBuild / version := "SAR-5"

ThisBuild / scalaVersion := "2.13.1"

ThisBuild / trapExit := false

val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.1.1",
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test,
  "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "com.google.inject" % "guice" % "4.2.2",
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "com.typesafe.akka" %% "akka-http" % "10.1.12",
  "com.typesafe.akka" %% "akka-stream" % "2.6.5",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12",
  "com.typesafe.akka" %% "akka-http-jackson" % "10.1.12"
)

lazy val root = (project in file(".")).settings(
  name := "Wizard",
  libraryDependencies ++= commonDependencies,
).aggregate(ResultTableModule, CardModule).dependsOn(ResultTableModule, CardModule) //% "compile->compile;test->test")

lazy val ResultTableModule = project.settings(
  name := "ResultTableModule",
  libraryDependencies ++= commonDependencies,
  libraryDependencies += "de.vandermeer" % "asciitable" % "0.3.2"
)

lazy val CardModule = project.settings(
  name :=  "CardModule",
  libraryDependencies ++= commonDependencies
)

coverageExcludedPackages := ".*gui.*"
coverageExcludedFiles := ".*Wizard.scala;.*WizardModule.scala"