package de.htwg.se.wizard.model.modelComponent

import de.htwg.se.wizard.model.PlayerInterface
import de.htwg.se.wizard.model.cards.Card


case class Player(name: String, playerCards: Option[List[Card]] = None) {
  override def toString: String = name
}

object Player extends PlayerInterface {
  def checkNumberOfPlayers(number: Int): Boolean = {
    if (number < 3 || number > 5) return false
    true
  }


  override def playerTurn(player: Player, round: Int): String = {

    val cards = player.playerCards.get


    val firstString = "Round " + round + " - Player: " + player.name
    val secondString = "Select one of the following cards:"

    firstString + "\n" + secondString + "\n" + "{ " + cards.mkString(", ") + " }"
  }

  override def playerPrediction(player: Player, round: Int, trump: Option[String]): String = {
    val firstString = "Round " + round + " - Player: " + player.name
    val secondString = "Trump Color: " + trump.getOrElse("None")
    val thirdString = "Your Cards: " + "{ " + player.playerCards.get.mkString(", ") + " }"
    val string = "Enter the amount of stitches you think you will get: "
    firstString + "\n" + secondString + "\n" + thirdString + "\n" + string

  }

  override def newPlayer(name: String): Player = {
    Player(name)
  }
}