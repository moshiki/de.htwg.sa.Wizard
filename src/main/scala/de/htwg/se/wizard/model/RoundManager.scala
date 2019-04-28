package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards.Card

class RoundManager {
  var needsSetup: Boolean = true
  var numberOfPlayers: Int = 0
  var players: List[Player] = Nil
  var currentPlayer: Int = 0
  var currentRound: Int = 0

  val initialCardStack: List[Card] = CardStack.initialize

  def roundsForThisGame:Int = {
    numberOfPlayers match {
      case 0 => 0
      case 3 => 20
      case 4 => 15
      case 5 => 12
      case _ => throw new IllegalArgumentException
    }
  }

  def nextPlayerSetup: Int = {
    if (currentPlayer < numberOfPlayers) currentPlayer + 1
    else 0
  }

  def nextPlayer: Int = {
    if (currentPlayer < numberOfPlayers - 1) currentPlayer + 1
    else 0
  }

  def getSetupStrings: String = {
    if (numberOfPlayers == 0) return "Welcome to Wizard!\nPlease enter the number of Players[3-5]:"
    if (players.size < numberOfPlayers) {
      currentPlayer = nextPlayerSetup
      return "Player " + currentPlayer + ", please enter your name:"
    }
    ""
  }

  def getPlayerStateStrings: String = {
    currentPlayer = nextPlayer
    if (currentPlayer == 0 && currentRound != roundsForThisGame) currentRound = currentRound + 1
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
        if (players.size == numberOfPlayers) needsSetup = false
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