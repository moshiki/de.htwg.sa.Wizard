package de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class PointsInnerTable(tag: Tag) extends Table[(Int, Int, Int)](tag, "PointsInnerTable"){
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def value = column[Int]("Value")

  def outerTableId = column[Int]("OuterTableId")

  override def * = (id, value, outerTableId)

  def outerTableForeignKey = foreignKey("PointsOuterTable_FK", outerTableId, TableQuery[PointsOuterTable])(_.id)
}
