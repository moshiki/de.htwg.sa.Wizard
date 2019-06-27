package de.htwg.se.wizard.model.modelComponent

import de.htwg.se.wizard.model.modelComponent.cards.StaticCard
import de.htwg.se.wizard.model.{CardInterface, PlayerInterface, StaticPlayerInterface}

import scala.xml.Elem


case class Player(name: String, playerCards: Option[List[CardInterface]] = None) extends PlayerInterface {
  override def toString: String = name

  override def getPlayerCards: Option[List[CardInterface]] = {
    playerCards
  }

  override def assignCards(cards: Option[List[CardInterface]]): PlayerInterface = {
    this.copy(playerCards = cards)
  }

  override def getName: String = {
    name
  }

  override def toXML: Elem = {
    <Player>
      <name>
        {name}
      </name>
      <playerCards>
        {if (playerCards.isDefined) playerCards.get.map(card => card.toXML)
      else "None"}
      </playerCards>
    </Player>
  }
}

case class StaticPlayer() extends StaticPlayerInterface {
  override def checkNumberOfPlayers(number: Int): Boolean = {
    if (number < 3 || number > 5) return false
    true
  }


  override def playerTurn(player: PlayerInterface, round: Int): String = {

    val cards = player.getPlayerCards.get


    val firstString = "Round " + round + " - Player: " + player.getName
    val secondString = "Select one of the following cards:"

    firstString + "\n" + secondString + "\n" + "{ " + cards.mkString(", ") + " }"
  }

  override def playerPrediction(player: PlayerInterface, round: Int, trump: Option[String]): String = {
    val firstString = "Round " + round + " - Player: " + player.getName
    val secondString = "Trump Color: " + trump.getOrElse("None")
    val thirdString = "Your Cards: " + "{ " + player.getPlayerCards.get.mkString(", ") + " }"
    val string = "Enter the amount of stitches you think you will get: "
    firstString + "\n" + secondString + "\n" + thirdString + "\n" + string

  }

  override def newPlayer(name: String): Player = {
    Player(name)
  }

  override def fromXML(node: scala.xml.Node): PlayerInterface = {
    val name = (node \ "name").text.trim
    val player = Player(name)

    val cards = node \ "playerCards"
    var playerCards: Option[List[CardInterface]] = None
    if (cards.text.trim != "None")  {
      val playerCardsList = cards.map(node => StaticCard().fromXML(node)).toList//.map(card => StaticCard().setOwner(card, player))
      playerCards = Some(playerCardsList)
    }

    player.copy(playerCards = playerCards)
  }
}