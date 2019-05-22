package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards.{Card, CardStack}
import de.htwg.se.wizard.util.ControllerUpdateStateObservable

import scala.collection.mutable.ListBuffer

case class RoundManager(numberOfPlayers: Int = 0, numberOfRounds: Int = 0) extends ControllerUpdateStateObservable {
  val initialCardStack: List[Card] = CardStack.initialize
  var shuffledCardStack = initialCardStack
  var players: List[Player] = Nil
  var currentPlayer: Int = 0
  var currentRound: Int = 1
  var predictionPerRound: List[Int] = Nil
  var nextRoundB: Boolean = true
  var mod: Int = 0

  def checkNumberOfPlayers(number: Int): Boolean = {
    Player.checkNumberOfPlayers(number)
  }

  def addPlayer(input: String): Unit = {
        updatePlayers(input)
        if (players.size == numberOfPlayers) triggerNextState()
  }

  def evaluate(selectedCard: Int): Unit = {
    // Put method that moves cards onto new stack here
    if(currentPlayer == numberOfPlayers -1) mod += 1




  }

  def shuffleCardStack(cardStack: List[Card]): List[Card] = {
    if(currentRound == 1) CardStack.shuffleCards(initialCardStack)
    val newCardStack = ListBuffer()
    for(card <- cardStack) {
      if(!card.hasOwner) newCardStack.+(card.toString)
    }
    newCardStack.toList
  }

  /*def collectStitch(playedCard: List[Card]): Int = {

  }

  def roundsResult(list: List[Int]): Unit = {
    for(i <- 0 until numberOfPlayers) {
      if(list(i) == )
    }
  }*/

  def updatePlayerPrediction(input: Int): Unit = {
    if(currentPlayer == 0) predictionPerRound = Nil
    predictionPerRound = predictionPerRound ::: List(input)
    if(predictionPerRound.size == numberOfPlayers) nextRoundB = false; mod = 0
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
    currentRound = nextRound
    if (currentRound == roundsForThisGame && currentPlayer == 0) {
      triggerNextState()
      return "\nGame Over! Press 'q' to quit."
    }
    if(predictionPerRound.size < numberOfPlayers || nextRoundB) {
      Player.playerPrediction(players(currentPlayer), currentRound)
    }
    else {
      Player.playerTurn(players(currentPlayer), currentRound, shuffledCardStack)
    }
  }

  def nextRound: Int = {
    if (currentPlayer == 0 && currentRound != roundsForThisGame && predictionPerRound.size == numberOfPlayers && mod != 1) {
      nextRoundB = true
      shuffledCardStack = shuffleCardStack(initialCardStack)
      currentRound + 1}
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