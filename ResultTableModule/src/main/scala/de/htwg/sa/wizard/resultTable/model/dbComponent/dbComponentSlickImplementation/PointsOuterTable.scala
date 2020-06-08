package de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentSlickImplementation

import slick.jdbc.MySQLProfile.api._

class PointsOuterTable(tag: Tag) extends Table[(Int, Int)](tag, "PointsOuterTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)
  def resultTableId = column[Int]("ResultTableId")
  def * = (id, resultTableId)

  def resultTableForeignKey = foreignKey("ResultTable_FK", resultTableId, TableQuery[ResultTableTable])(_.id)
}
