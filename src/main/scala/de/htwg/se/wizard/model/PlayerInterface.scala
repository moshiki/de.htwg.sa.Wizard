package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.modelComponent.Player

trait PlayerInterface {

  def newPlayer(name: String): Player

  def playerPrediction(player: Player, round: Int, trump: Option[String]): String

  def playerTurn(player: Player, round: Int): String


}
