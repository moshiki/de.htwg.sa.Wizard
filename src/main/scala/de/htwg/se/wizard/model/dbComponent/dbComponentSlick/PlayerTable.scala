package de.htwg.se.wizard.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class PlayerTable(tag: Tag) extends Table[(Int, String)](tag, "PlayerTable"){
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("Name")

  def * = (id, name)

  //def roundManagerForeignKey = foreignKey("")
}
