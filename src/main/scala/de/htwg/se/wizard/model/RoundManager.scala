package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards.Card

class RoundManager {
  var needsSetup: Boolean = true
  var players: IndexedSeq[Player] = IndexedSeq()
  var roundNumbers: Int = 0
  var currentPlayer: Int = 0
  val numberOfRounds: Int = {
    players.size match {
      case 0 => 0
      case 3 => 20
      case 4 => 15
      case 5 => 12
      case _ => throw new IllegalArgumentException
    }
  }

  val initialCardStack: List[Card] = CardStack.initialize

  def nextPlayer: Int = {
    if (currentPlayer == players.size - 1) 0
    else currentPlayer + 1
  }

  val getCurrentState: (Int, Int, Boolean) = {
    (currentPlayer, needsSetup)
  }

  def currentStateToString: String = {
    "Hello, this is a test."
  }
}
