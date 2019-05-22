package de.htwg.se.wizard.model
import de.htwg.se.wizard.model.cards.Card

case class Player(name: String, playerPrediction: Option[List[Int]] = None, var playerCards: Option[IndexedSeq[Card]] = None) {
  override def toString: String = name
}

object Player {
  def checkNumberOfPlayers(number: Int): Boolean = {
    if (number < 3 || number > 5) return false
    true
  }

  def playerTurn(player: Player, round: Int, cardStack: List[Card]): String = {
    val indexGenerator = scala.util.Random
    val cards = for {_ <- 1 to round} yield cardStack(indexGenerator.nextInt(cardStack.size - 1))
    Player(player.name,None, Some(cards))

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