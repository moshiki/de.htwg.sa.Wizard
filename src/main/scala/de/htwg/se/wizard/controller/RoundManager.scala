package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.cards.{Card, CardStack, DefaultCard}
import de.htwg.se.wizard.model.{Player, ResultTable}

import scala.collection.mutable.ListBuffer

case class RoundManager(numberOfPlayers: Int = 0, numberOfRounds: Int = 0, shuffledCardStack: List[Card] = Nil,
                        players: List[Player] = Nil, currentPlayer: Int = 0, currentRound: Int = 1,
                        predictionPerRound: List[Int] = Nil, stitchesPerRound: Map[String, Int] = Map.empty[String, Int],
                        playedCards: List[Card] = Nil, predictionMode:Boolean = true,
                        cleanMap: Map[String, Int] = Map.empty[String, Int]) {
  val initialCardStack: List[Card] = CardStack.initialize
  val resultTable: ResultTable = ResultTable(roundsForThisGame, numberOfPlayers)

  def checkNumberOfPlayers(number: Int): Boolean = {
    Player.checkNumberOfPlayers(number)
  }

  def addPlayer(name: String): RoundManager = {
    val newPlayer = Player(name)
    val oldPlayerList = this.players
    if (oldPlayerList contains newPlayer) return this
    val newPlayerList = oldPlayerList ::: List(newPlayer)

    var newStitchesPerRound = collection.mutable.Map() ++ stitchesPerRound
    newStitchesPerRound += name -> 0

    this.copy(players = newPlayerList, stitchesPerRound = newStitchesPerRound.toMap)

    // TODO: triggerNextState and assign cleanMap (do this in controller)
  }

  def playCard(selectedCard: Int): RoundManager = {
    val player = players(currentPlayer)
    val currentPlayersCards = player.playerCards.get.to[ListBuffer]
    val playedCard = currentPlayersCards.remove(selectedCard - 1)

    val newPlayers = players.to[ListBuffer]
    newPlayers.update(currentPlayer, player.copy(playerCards = Some(currentPlayersCards.toList)))

    this.copy(playedCards = playedCard :: playedCards, players = newPlayers.toList)
  }


  def cardDistribution(): RoundManager = {
    var list = List[Card]()
    val stack = shuffledCardStack.to[ListBuffer]
    for(_ <- 1 to currentRound) {
      val card = stack.remove(0)
      val typ = Card.setOwner(card, players(currentPlayer))
      list = list ::: List[Card](typ)
    }

    val newPlayer = players(currentPlayer).copy(playerCards = Some(list))
    val newPlayers = players.to[ListBuffer]
    newPlayers.update(currentPlayer, newPlayer)

    this.copy(shuffledCardStack = stack.toList, players = newPlayers.toList)

    // FIXME: Probably need to do this for all players and not only the current one.
    //  It's also possible that this method's call needs to move from getPlayerStateStrings to Controller.
  }

  def updatePlayerPrediction(input: Int): Unit = {
    predictionPerRound = predictionPerRound ::: List(input)
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
      return "\nGame Over! Press 'q' to quit.\n" + resultTable.toString
    }
    if(predictionPerRound.size < numberOfPlayers) {
      predictionMode = true
      cardDistribution()
      var out = "\n"
      if (currentPlayer == 0) out += resultTable.toString + "\n"
      out += Player.playerPrediction(players(currentPlayer), currentRound, trumpColor)
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
    val stitchPlayer = CardStack.getPlayerOfHighestCard(playedCards.reverse, trumpColor)
    playedCards = Nil
    val mutMap = collection.mutable.Map() ++ stitchesPerRound
    mutMap.put(stitchPlayer.name, mutMap(stitchPlayer.name) + 1)
    stitchesPerRound = mutMap.toMap
    mutMap(stitchPlayer.name)
  }

  def pointsForRound():Unit = {
    for (i <- players.indices) {
      resultTable.updatePoints(currentRound, i,
        RoundManager.calcPoints(predictionPerRound(i), stitchesPerRound(players(i).name)))
    }
  }
}

object RoundManager {
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