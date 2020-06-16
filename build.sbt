import sbt.Keys.libraryDependencies

//name := "Wizard"

ThisBuild / version := "SAR-6"

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
  "com.typesafe.akka" %% "akka-http-jackson" % "10.1.12",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.32.0",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "org.slf4j" % "slf4j-nop" % "1.7.30",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "mysql" % "mysql-connector-java" % "8.0.20",
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.0.4",
)

lazy val root = (project in file(".")).settings(
  name := "Wizard",
  libraryDependencies ++= commonDependencies,
  assemblyMergeStrategy in assembly := {
    case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case "application.conf"                            => MergeStrategy.concat
    case "module-info.class"                           => MergeStrategy.concat
    case "CHANGELOG.adoc"                              => MergeStrategy.concat
    case "unwanted.txt"                                => MergeStrategy.discard
    case PathList(ps @ _*) if ps.last endsWith ".proto" => MergeStrategy.first
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  mainClass in assembly := Some("de.htwg.se.wizard.Wizard")
).aggregate(ResultTableModule, CardModule).dependsOn(ResultTableModule, CardModule) //% "compile->compile;test->test")

lazy val ResultTableModule = project.settings(
  name := "ResultTableModule",
  libraryDependencies ++= commonDependencies,
  libraryDependencies += "de.vandermeer" % "asciitable" % "0.3.2",
  assemblyMergeStrategy in assembly := {
    case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case "application.conf"                            => MergeStrategy.concat
    case "module-info.class"                           => MergeStrategy.concat
    case "CHANGELOG.adoc"                              => MergeStrategy.concat
    case "unwanted.txt"                                => MergeStrategy.discard
    case PathList(ps @ _*) if ps.last endsWith ".proto" => MergeStrategy.first
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  mainClass in assembly := Some("de.htwg.sa.wizard.resultTable.ResultTable")
)

lazy val CardModule = project.settings(
  name :=  "CardModule",
  libraryDependencies ++= commonDependencies,
  assemblyMergeStrategy in assembly := {
    case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case "application.conf"                            => MergeStrategy.concat
    case "module-info.class"                           => MergeStrategy.concat
    case "CHANGELOG.adoc"                              => MergeStrategy.concat
    case "unwanted.txt"                                => MergeStrategy.discard
    case PathList(ps @ _*) if ps.last endsWith ".proto" => MergeStrategy.first
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  mainClass in assembly := Some("de.htwg.sa.wizard.cardModule.CardMod")
)

coverageExcludedPackages := ".*gui.*"
coverageExcludedFiles := ".*Wizard.scala;.*WizardModule.scala"