name := "Wizard"

version := "0.13"

scalaVersion := "2.12.8"

trapExit := false

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1"

libraryDependencies += "com.google.inject" % "guice" % "4.1.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0"

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.1"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.4"