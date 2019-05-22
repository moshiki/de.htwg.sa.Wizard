package de.htwg.se.wizard.model
import de.htwg.se.wizard.model.cards.{Card, DefaultCard, JesterCard, WizardCard}

import scala.util.Random


case class Player(name: String, playerPrediction: Option[List[Int]] = None, var playerCards: Option[List[Card]] = None) {
  override def toString: String = name
}

object Player {
  def checkNumberOfPlayers(number: Int): Boolean = {
    if (number < 3 || number > 5) return false
    true
  }

  def playerTurn(player: Player, round: Int, cardStack: List[Card]): String = {
    var list = List[Card]()
    for(i <- 1 to round) {
      val random = Random.nextInt(cardStack.size)
      val card = cardStack(random)
      list = list ::: List[Card](card)
    }

    player.playerCards = Some(list)


    val firstString = "Round " + round + " - Player: " + player.name
    val secondString = "Select one of the following cards:"

    firstString + "\n" + secondString + "\n" + "{ " + list.mkString(", ") + " }"
  }

  def playerPrediction(player: Player, round: Int): String = {
    val firstString = "Round " + round + " - Player: " + player.name
    val string = "Enter the amount of stitches you think you will get: "
    firstString + "\n" + string

  }
}
/*if(card.isWizard) WizardCard().copy(owner = Some(player)),  list = list ::: List[Card](card)
      if(card.isJester) JesterCard().copy(owner = Some(player)),  list = list ::: List[Card](card)*/