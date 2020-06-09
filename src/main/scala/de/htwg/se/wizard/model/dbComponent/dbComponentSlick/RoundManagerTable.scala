package de.htwg.se.wizard.model.dbComponent.dbComponentSlick

import slick.jdbc.MySQLProfile.api._

class RoundManagerTable(tag: Tag) extends Table[(Int, Int, Int, Int, Int, Boolean)](tag, "RoundManagerTable") {
  def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

  def numberOfPlayers = column[Int]("NumberOfPlayers")

  def numberOfRounds = column[Int]("NumberOfRounds")

  def currentPlayerNumber = column[Int]("CurrentPlayerNumber")

  def currentRound = column[Int]("CurrentRound")

  def predictionMode = column[Boolean]("PredictionMode")

  def * = (id, numberOfPlayers, numberOfRounds, currentPlayerNumber, currentRound, predictionMode)
}
