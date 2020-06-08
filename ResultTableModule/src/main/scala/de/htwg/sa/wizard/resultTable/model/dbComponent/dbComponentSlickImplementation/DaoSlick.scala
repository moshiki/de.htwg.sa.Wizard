package de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentSlickImplementation

import de.htwg.sa.wizard.resultTable.model.dbComponent.DaoInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class DaoSlick() extends DaoInterface {
  // TODO: Docker-ENV for Docker-Database

  val database = Database.forURL(
    url = "jdbc:mysql://localhost:3306/resultTable?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "wizard",
    password = "wizard"
  )

  val resultTableTable = TableQuery[ResultTableTable]
  val pointsOuterTable = TableQuery[PointsOuterTable]
  val pointsInnerTable = TableQuery[PointsInnerTable]
  val playerNameTable = TableQuery[PlayerNameTable]
  val setup = DBIO.seq((resultTableTable.schema
    ++ pointsOuterTable.schema
    ++ pointsInnerTable.schema
    ++ playerNameTable.schema).createIfNotExists)
  database.run(setup)

  override def getLatestGame(resultTableInterface: ResultTableInterface): ResultTableInterface = {
    val latestResultTableQuery = resultTableTable.sortBy(resultTable => resultTable.id.desc).take(1).result.head
    val latestResultTable = Await.result(database.run(latestResultTableQuery), Duration.Inf)

    val pointsOuterVectorQuery = pointsOuterTable.filter(_.resultTableId === latestResultTable._1).map(_.id).result
    val pointsOuterVectorIds = Await.result(database.run(pointsOuterVectorQuery), Duration.Inf)

    val pointsVector = pointsOuterVectorIds.map(id => {
      val pointsQuery = pointsInnerTable.filter(points => points.outerTableId === id).map(innerTable => innerTable.value).result
      Await.result(database.run(pointsQuery), Duration.Inf).toVector
    }).toVector

    val playerNamesQuery = playerNameTable.filter(_.resultTableId === latestResultTable._1).map(_.playerName).result
    val playerNamesList = Await.result(database.run(playerNamesQuery), Duration.Inf).toList

    resultTableInterface.recreateWithData(latestResultTable._2, latestResultTable._3, pointsVector, playerNamesList)
  }

  override def saveGame(resultTableInterface: ResultTableInterface): Unit = {
    Await.ready(database.run(resultTableTable += (0, resultTableInterface.roundsToPlay,resultTableInterface.numberOfPlayers)), Duration.Inf)
    val resultTableIdQuery = resultTableTable.sortBy(_.id.desc).take(1).map(_.id).result.head
    val resultTableId = Await.result(database.run(resultTableIdQuery), Duration.Inf)

    Await.ready(database.run(playerNameTable ++= resultTableInterface.playerNames.map(name => (0, name, resultTableId))), Duration.Inf)

    resultTableInterface.points.foreach(outerVector => {
      Await.ready(database.run(pointsOuterTable += (0, resultTableId)), Duration.Inf)
      val pointsOuterTableIdQuery = pointsOuterTable.sortBy(_.id.desc).take(1).map(_.id).result.head
      val outerTableId = Await.result(database.run(pointsOuterTableIdQuery), Duration.Inf)

      Await.ready(database.run(pointsInnerTable ++= outerVector.map(point => (0, point, outerTableId))), Duration.Inf)
    })
  }
}
