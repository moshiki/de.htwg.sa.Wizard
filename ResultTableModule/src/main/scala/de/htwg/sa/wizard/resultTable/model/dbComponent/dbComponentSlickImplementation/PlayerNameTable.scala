package de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentSlickImplementation

import slick.jdbc.MySQLProfile.api._

class PlayerNameTable(tag: Tag) extends Table[(Int, String, Int)] (tag, "PlayerNameTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def playerName = column[String]("PlayerName")

  def resultTableId = column[Int]("ResultTableId")

  def * = (id, playerName, resultTableId)

  def resultTableForeignKey = foreignKey("ResultTableForeignKey", resultTableId, TableQuery[ResultTableTable])(_.id)
}
