package de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class ResultTableTable(tag: Tag) extends Table[(Int, Int, Int)](tag, "ResultTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def roundsToPlay = column[Int]("RoundsToPlay")

  def numberOfPlayers = column[Int]("NumberOfPlayers")

  def * = (id, roundsToPlay, numberOfPlayers)
}
