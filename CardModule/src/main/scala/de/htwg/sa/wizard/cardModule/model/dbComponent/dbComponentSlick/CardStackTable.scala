package de.htwg.sa.wizard.cardModule.model.dbComponent.dbComponentSlick

import java.sql.Timestamp

import slick.jdbc.MySQLProfile.api._

class CardStackTable(tag: Tag) extends Table[(Int, Timestamp)] (tag, "CardStackTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def timestamp = column[Timestamp]("Timestamp")

  def * = (id, timestamp)
}
