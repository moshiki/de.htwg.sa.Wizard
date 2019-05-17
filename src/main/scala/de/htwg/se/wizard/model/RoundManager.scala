package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards.{Card, CardStack}
import de.htwg.se.wizard.util.ControllerUpdateStateObservable

case class RoundManager(numberOfPlayers: Int = 0, numberOfRounds: Int = 0) extends ControllerUpdateStateObservable {
  val initialCardStack: List[Card] = CardStack.initialize
  var players: List[Player] = Nil
  var currentPlayer: Int = 0
  var currentRound: Int = 0
  var predictionPerRound: List[Int] = Nil

  def checkNumberOfPlayers(number: Int): Boolean = {
    Player.checkNumberOfPlayers(number)
  }

  def addPlayer(input: String): Unit = {
        updatePlayers(input)
        if (players.size == numberOfPlayers) triggerNextState()
  }

  def evaluate(selectedCard: Int): Unit = {
    // Put method that moves cards onto new stack here
  }

  def updatePlayerPrediction(input: Int): Unit = {
    if(currentPlayer == 0) predictionPerRound = Nil
    predictionPerRound = predictionPerRound ::: List(input)
  }

  def updatePlayers(input: String): Unit = {
    players = players ::: List(Player(input))
  }

  def getSetupStrings: String = {
    currentPlayer = nextPlayerSetup
    "Player " + currentPlayer + ", please enter your name:"
  }

  def nextPlayerSetup: Int = {
    if (currentPlayer < numberOfPlayers) currentPlayer + 1
    else 0
  }

  def getPlayerStateStrings: String = {
    currentPlayer = nextPlayer
    if (currentRound == roundsForThisGame && currentPlayer == 0) {
      triggerNextState()
      return "\nGame Over! Press 'q' to quit."
    }
    currentRound = nextRound
    if(predictionPerRound.size < numberOfPlayers) {
      Player.playerPrediction(players(currentPlayer), currentRound)
    }
    else {
      Player.playerTurn(players(currentPlayer), currentRound, initialCardStack)
    }



  }

  def nextRound: Int = {
    if (currentPlayer == 0 && currentRound != roundsForThisGame) currentRound + 1
    else currentRound
  }

  def roundsForThisGame: Int = {
    numberOfPlayers match {
      case 0 => 0
      case 3 => 20
      case 4 => 15
      case 5 => 12
      case _ => throw new IllegalArgumentException
    }
  }

  def nextPlayer: Int = {
    if (currentPlayer < numberOfPlayers - 1) currentPlayer + 1
    else 0
  }
}

object RoundManager {
  case class Builder() {
    var numberOfPlayers:Int = 0
    var numberOfRounds:Int = 0

    def withNumberOfPlayers(players: Int): Builder = {
      numberOfPlayers = players
      this
    }

    def withNumberOfRounds(rounds: Int): Builder = {
      numberOfRounds = rounds
      this
    }

    def build(): RoundManager = {
      RoundManager(numberOfPlayers, numberOfRounds)
    }
  }
}