name := "Wizard"

version := "0.13"

scalaVersion := "2.13.1"

trapExit := false

libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "4.4.0" % Test

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1"

libraryDependencies += "com.google.inject" % "guice" % "4.2.2"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.6"


libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.3.0"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1"

// https://mvnrepository.com/artifact/de.vandermeer/asciitable
libraryDependencies += "de.vandermeer" % "asciitable" % "0.3.2"

