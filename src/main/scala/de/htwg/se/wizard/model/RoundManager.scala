package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards.Card

class RoundManager {
  var needsSetup: Boolean = true
  var players: IndexedSeq[Player] = IndexedSeq()
  var roundNumbers: Int = 0
  var currentPlayer: Int = 0
  var currentRound: Int = 0
  var numberOfRounds: Int = {
    players.size match {
      case 0 => 0
      case 3 => 20
      case 4 => 15
      case 5 => 12
      case _ => throw new IllegalArgumentException
    }
  }

  val initialCardStack: List[Card] = CardStack.initialize

}
