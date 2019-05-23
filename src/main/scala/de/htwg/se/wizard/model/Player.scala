package de.htwg.se.wizard.model
import de.htwg.se.wizard.model.cards.{Card, DefaultCard, JesterCard, WizardCard}

import scala.collection.mutable.ListBuffer


case class Player(name: String, var playerCards: Option[ListBuffer[Card]] = None) {
  override def toString: String = name
}

object Player {
  def checkNumberOfPlayers(number: Int): Boolean = {
    if (number < 3 || number > 5) return false
    true
  }


  def playerTurn(player: Player, round: Int): String = {

    val cards = player.playerCards


    val firstString = "Round " + round + " - Player: " + player.name
    val secondString = "Select one of the following cards:"

    firstString + "\n" + secondString + "\n" + "{ " + cards.mkString(", ") + " }"
  }

  def playerPrediction(player: Player, round: Int): String = {
    val firstString = "Round " + round + " - Player: " + player.name
    val string = "Enter the amount of stitches you think you will get: "
    firstString + "\n" + string

  }
}