package de.htwg.se.wizard.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class ControllerStateTable(tag: Tag) extends Table[(Int, String)](tag, "ControllerStateTable"){
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def state = column[String]("State")

  def * = (id, state)
}
