package de.htwg.se.wizard.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class TricksPerRoundTable(tag: Tag) extends Table[(Int, String, Int, Int)](tag, "TricksPerRoundTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def playerName = column[String]("PlayerName")

  def tricks = column[Int]("Tricks")

  def roundManagerId = column[Int]("RoundManagerId")

  def * = (id, playerName, tricks, roundManagerId)

  def roundManagerForeignKey = foreignKey("TricksPerRoundTableRoundManager_FK", roundManagerId, TableQuery[RoundManagerTable])(_.id)
}
