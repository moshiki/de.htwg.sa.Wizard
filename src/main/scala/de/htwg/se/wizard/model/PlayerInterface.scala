package de.htwg.se.wizard.model

trait PlayerInterface {

  def newPlayer(name: String): Player

  def playerPrediction(player: Player, round: Int, trump: Option[String]): String

  def playerTurn(player: Player, round: Int): String


}
