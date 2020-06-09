package de.htwg.se.wizard.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class CardTable(tag: Tag) extends Table[(Int, String, Option[String], Option[String], Option[Int], Option[Int], Option[Int])] (tag, "CardTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def cardType = column[String]("CardType")

  def color = column[Option[String]]("Color")

  def owner = column[Option[String]]("Owner")

  def value = column[Option[Int]]("Value")

  def playerId = column[Option[Int]]("PlayerId")

  def roundManagerId = column[Option[Int]]("RoundManagerId")

  def * = (id, cardType, color, owner, value, playerId, roundManagerId)

  def playerTableForeignKey = foreignKey("CardPlayer_FK", playerId, TableQuery[PlayerTable])(_.id)

  def roundManagerTableForeignKey = foreignKey("CardRoundManager_FK", roundManagerId, TableQuery[RoundManagerTable])(_.id)
}
