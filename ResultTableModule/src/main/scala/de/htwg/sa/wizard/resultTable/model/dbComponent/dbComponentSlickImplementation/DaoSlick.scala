package de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentSlickImplementation

import de.htwg.sa.wizard.resultTable.model.dbComponent.DaoInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

case class DaoSlick() extends DaoInterface {
  val database = Database.forConfig("mysql")/*Database.forURL(
    url = "jdbc:mysql://localhost:3306/resultTable",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "wizard",
    password = "wizard"
  )*/
  //val setupDatabase = DBIO.seq((TableQuery[ResultTableTable].schema ++ TableQuery[PointsOuterTable].schema ++ TableQuery[PointsInnerTable].schema).create)

  val resultTableTable = TableQuery[ResultTableTable]
  val pointsOuterTable = TableQuery[PointsOuterTable]
  val pointsInnerTable = TableQuery[PointsInnerTable]
  val setup = DBIO.seq((resultTableTable.schema ++ pointsOuterTable.schema ++ pointsInnerTable.schema).createIfNotExists)
  database.run(setup)

  override def getLatestGame: ResultTableInterface = ???

  override def saveGame(daoResultTable: ResultTableTable): Unit = ???
}
