package de.htwg.sa.wizard.cardModule.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class CardTable(tag: Tag) extends Table[(Int, String, Option[String], Option[String], Option[Int], Int)] (tag, "CardTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def cardType = column[String]("CardType")

  def color = column[Option[String]]("Color")

  def owner = column[Option[String]]("Owner")

  def value = column[Option[Int]]("Value")

  def cardStackId = column[Int]("cardStackId")

  def * = (id, cardType, color, owner, value, cardStackId)

  def cardStackIdForeignKey = foreignKey("CardStackId_FK", cardStackId, TableQuery[CardStackTable])(_.id)
}
