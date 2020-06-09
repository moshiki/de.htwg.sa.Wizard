package de.htwg.se.wizard.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class PredictionPerRoundTable(tag: Tag) extends Table[(Int, Int, Int)](tag, "PredictionPerRoundTable") {
  def id = column[Int]("Id")

  def prediction = column[Int]("Prediction")

  def roundManagerId = column[Int]("RoundManagerId")

  def * = (id, prediction, roundManagerId)

  def roundManagerForeignKey = foreignKey("PredictionPerRoundRoundManager_FK", roundManagerId, TableQuery[RoundManagerTable])(_.id)
}
