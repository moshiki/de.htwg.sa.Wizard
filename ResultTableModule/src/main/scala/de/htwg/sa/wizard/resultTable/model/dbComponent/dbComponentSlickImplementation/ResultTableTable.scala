package de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentSlickImplementation

import slick.jdbc.MySQLProfile.api._

class ResultTableTable(tag: Tag) extends Table[(Int, String)](tag, "ResultTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def resultTableContents = column[String]("ResultTableContents")

  def * = (id, resultTableContents)
}
