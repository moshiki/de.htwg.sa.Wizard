package de.htwg.se.wizard.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class PlayerTable(tag: Tag) extends Table[(Int, String, Int)](tag, "PlayerTable"){
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("Name")

  def roundManagerId = column[Int]("RoundManagerId")

  def * = (id, name, roundManagerId)

  def roundManagerForeignKey = foreignKey("ResultTablePlayerTable_FK", roundManagerId, TableQuery[RoundManagerTable])(_.id)
}
