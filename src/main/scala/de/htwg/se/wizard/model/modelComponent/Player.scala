package de.htwg.se.wizard.model.modelComponent

import de.htwg.se.wizard.model.{CardInterface, PlayerInterface, StaticPlayerInterface}


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

  override def toXML: String = {
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
}