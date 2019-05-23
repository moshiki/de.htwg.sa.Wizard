package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards.{Card, CardStack, DefaultCard}
import de.htwg.se.wizard.util.ControllerUpdateStateObservable

import scala.collection.mutable.ListBuffer

case class RoundManager(numberOfPlayers: Int = 0, numberOfRounds: Int = 0) extends ControllerUpdateStateObservable {
  val initialCardStack: List[Card] = CardStack.initialize
  var shuffledCardStack:ListBuffer[Card] = CardStack.shuffleCards(initialCardStack)
  var players: List[Player] = Nil
  var currentPlayer: Int = 0
  var currentRound: Int = 1
  var predictionPerRound: List[Int] = Nil
  var stitchesPerRound: List[Int] = Nil
  var playedCards: List[Card] = Nil
  var predictionMode:Boolean = true

  def checkNumberOfPlayers(number: Int): Boolean = {
    Player.checkNumberOfPlayers(number)
  }

  def addPlayer(input: String): Unit = {
        updatePlayers(input)
        if (players.size == numberOfPlayers) triggerNextState()
  }

  def evaluate(selectedCard: Int): Unit = {
    playedCards = players(currentPlayer).playerCards.get.remove(selectedCard - 1) :: playedCards
  }

  def calcPoints(playerPrediction: Int, stitches: Int): Int = {
    var points = 0
    for(_ <- 1 to stitches) points += 10
    if(playerPrediction == stitches) {
      points = 20
    }
    if(playerPrediction < stitches) for(_ <- playerPrediction to stitches) {points -= 10}
    if(playerPrediction > stitches) for (_ <- stitches to playerPrediction) {points -= 10}
    points
  }



  def cardDistribution(): List[Card] = {
    var list = List[Card]()
    for(_ <- 1 to currentRound) {
      val card = shuffledCardStack.remove(0)
      val typ = Card.setOwner(card, players(currentPlayer))
      list = list ::: List[Card](typ)
    }
    players(currentPlayer).playerCards = Some(list.to[ListBuffer])
    list
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
    if (!predictionMode) currentRound = nextRound
    if (currentRound == roundsForThisGame && currentPlayer == 0) {
      triggerNextState()
      return "\nGame Over! Press 'q' to quit."
    }
    if(predictionPerRound.size < numberOfPlayers) {
      predictionMode = true
      cardDistribution()
      Player.playerPrediction(players(currentPlayer), currentRound)
    } else {
      predictionMode = false
      Player.playerTurn(players(currentPlayer), currentRound)
    }
  }

  def nextRound: Int = {
    if (currentPlayer == 0 && currentRound != roundsForThisGame && players.last.playerCards.get.isEmpty) {
      shuffledCardStack = CardStack.shuffleCards(initialCardStack)
      predictionPerRound = Nil
      stitchInThisCycle
      currentRound + 1
    }
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

  def trumpColor: Option[String] = {
    val topCard = shuffledCardStack.head
    topCard match {
      case card: DefaultCard => Some(card.color)
      case _ => None
    }
  }

  def stitchInThisCycle: Int = {
    CardStack.getPlayerOfHighestCard(playedCards, trumpColor)
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