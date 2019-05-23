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
  var stitchesPerRound: Map[String, Int] = Map.empty[String, Int]
  var playedCards: List[Card] = Nil
  var predictionMode:Boolean = true
  var cleanMap: Map[String, Int] = Map.empty[String, Int]
  val resultTable: ResultTable = ResultTable(roundsForThisGame, numberOfPlayers)

  def checkNumberOfPlayers(number: Int): Boolean = {
    Player.checkNumberOfPlayers(number)
  }

  def addPlayer(input: String): Unit = {
        updatePlayers(input)
        var mutMap = collection.mutable.Map() ++ stitchesPerRound
        mutMap += input -> 0
        stitchesPerRound = mutMap.toMap
        if (players.size == numberOfPlayers) {
          cleanMap = stitchesPerRound
          triggerNextState()
        }
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
      var out = "\n"
      if (currentPlayer == 0) out += resultTable.toString + "\n"
      out += Player.playerPrediction(players(currentPlayer), currentRound)
      out
    } else {

      predictionMode = false
      Player.playerTurn(players(currentPlayer), currentRound)
    }
  }

  def nextRound: Int = {
    if (currentPlayer == 0 && currentRound != roundsForThisGame && players.last.playerCards.get.isEmpty) {
      pointsForRound()
      shuffledCardStack = CardStack.shuffleCards(initialCardStack)
      predictionPerRound = Nil
      stitchesPerRound = cleanMap
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
    else {
      if (!predictionMode) stitchInThisCycle
      0
    }
  }

  def trumpColor: Option[String] = {
    val topCard = shuffledCardStack.head
    topCard match {
      case card: DefaultCard => Some(card.color)
      case _ => None
    }
  }

  def stitchInThisCycle: Int = {
    val stitchPlayer = CardStack.getPlayerOfHighestCard(playedCards, trumpColor)
    val mutMap = collection.mutable.Map() ++ stitchesPerRound
    mutMap.put(stitchPlayer.name, mutMap(stitchPlayer.name) + 1)
    stitchesPerRound = mutMap.toMap
    mutMap(stitchPlayer.name)
  }

  def pointsForRound():Unit = {
    for (i <- players.indices) {
      resultTable.updatePoints(currentRound, i, calcPoints(predictionPerRound(i), stitchesPerRound(players(i).name)))
    }
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