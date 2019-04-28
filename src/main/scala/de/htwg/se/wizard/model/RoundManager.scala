package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards.Card

class RoundManager {
  var needsSetup: Boolean = true
  var numberOfPlayers: Int = 0
  var players: List[Player] = List()
  var currentPlayer: Int = 0
  var currentRound: Int = 0
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
    if (currentPlayer < numberOfPlayers) currentPlayer + 1
    else 0
  }

  def getSetupStrings: String = {
    if (numberOfPlayers == 0) return "Welcome to Wizard!\nPlease enter the number of Players[1-6]:"
    if (players.size < numberOfPlayers) {
      if (players.size + 1 == numberOfPlayers) needsSetup = false
      currentPlayer = nextPlayer
      return "Player " + currentPlayer + "Please enter your name: "
    }
    ""
  }

  def getPlayerStateStrings: String = {
    currentPlayer = nextPlayer
    currentRound = currentRound + 1
    Player.playerTurn(players(currentPlayer), currentRound, initialCardStack)
  }

  def eval(input: String): Unit = {
    if (needsSetup) {
      if (numberOfPlayers == 0) {
        val number = RoundManager.toInt(input)
        if (number.isEmpty) return
        numberOfPlayers = Player.getNumberOfPlayers(number.get)
      } else {
        updatePlayers(input)
      }
    } else {
      // Put method that moves cards onto new stack here
    }
  }

  def updatePlayers(input: String): Unit = {
    players = players ::: List(Player(input))
  }

  def currentStateToString: String = {
    if (needsSetup) {
      getSetupStrings
    } else {
      getPlayerStateStrings
    }
  }
}

object RoundManager {
  def toInt(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case _: Exception => None
    }
  }
}