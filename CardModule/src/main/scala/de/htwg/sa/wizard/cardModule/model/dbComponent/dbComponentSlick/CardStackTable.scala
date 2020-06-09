package de.htwg.sa.wizard.cardModule.model.dbComponent.dbComponentSlick

import java.sql.Date
import slick.jdbc.MySQLProfile.api._

class CardStackTable(tag: Tag) extends Table[(Int, Date)] (tag, "CardStackTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def date = column[Date]("Date")

  def * = (id, date)
}
