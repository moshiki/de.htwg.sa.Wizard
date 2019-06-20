package de.htwg.se.wizard.controller.maincontroller

import de.htwg.se.wizard.model.cards.Card
import de.htwg.se.wizard.model.PlayerInterface
import de.htwg.se.wizard.model.modelComponent.{Player, ResultTable}

import scala.collection.mutable.ListBuffer

case class RoundManager(numberOfPlayers: Int = 0,
                        numberOfRounds: Int = 0,
                        shuffledCardStack: List[Card] = Card.shuffleCards(Card.initializeCardStack()),
                          //CardStack.shuffleCards(CardStack.initialize),
                        players: List[Player] = Nil,
                        currentPlayer: Int = 0,
                        currentRound: Int = 1,
                        predictionPerRound: List[Int] = Nil,
                        stitchesPerRound: Map[String, Int] = Map.empty[String, Int],
                        playedCards: List[Card] = Nil,
                        predictionMode:Boolean = true,
                        cleanMap: Map[String, Int] = Map.empty[String, Int],
                        resultTable: ResultTable) {
  val initialCardStack: List[Card] = Card.initializeCardStack()

  def checkNumberOfPlayers(number: Int): Boolean = {
    Player.checkNumberOfPlayers(number)
  }

  def addPlayer(name: String): RoundManager = {

    val newPlayer =  Player(name)
    val oldPlayerList = this.players
    if (oldPlayerList contains newPlayer) return this
    val newPlayerList = oldPlayerList ::: List(newPlayer)

    var newStitchesPerRound = collection.mutable.Map() ++ stitchesPerRound
    newStitchesPerRound += name -> 0

    this.copy(players = newPlayerList, stitchesPerRound = newStitchesPerRound.toMap)
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

    //todo: Probably need to do this for all players and not only the current one.
  }

  def updatePlayerPrediction(input: Int): RoundManager = {
    this.copy(predictionPerRound = predictionPerRound ::: List(input))
  }

  def getSetupStrings: String = {
    "Player " + currentPlayer + ", please enter your name:"
  }

  def nextPlayerSetup: Int = {
    if (currentPlayer < numberOfPlayers) currentPlayer + 1
    else 0
  }

  def getPlayerStateStrings: String = {
    if (currentRound == numberOfRounds && currentPlayer == 0) {
      return "\nGame Over! Press 'q' to quit.\n" + resultTable.toString
    }
    if(predictionPerRound.size < numberOfPlayers) {
      var out = "\n"
      if (currentPlayer == 0) out += resultTable.toString + "\n"
      out += Player.playerPrediction(players(currentPlayer), currentRound, trumpColor)
      out
    } else {
      Player.playerTurn(players(currentPlayer), currentRound)
    }
  }

  def nextRound: RoundManager = {
    if (currentPlayer == 0 && currentRound != numberOfRounds && players.last.playerCards.get.isEmpty) {
      this.copy(
        resultTable = pointsForRound(),
        shuffledCardStack = Card.shuffleCards(initialCardStack),
        //shuffledCardStack = CardStack.shuffleCards(initialCardStack),
        predictionPerRound = Nil,
        stitchesPerRound = cleanMap,
        currentRound = currentRound + 1
      )
    }
    else this
  }


  def nextPlayer: RoundManager = {
    if (currentPlayer < numberOfPlayers - 1) this.copy(currentPlayer = currentPlayer + 1)
    else {
      var newRoundManager = this
      if (!predictionMode) newRoundManager = stitchInThisCycle
      newRoundManager.copy(currentPlayer = 0)
    }
  }

  def trumpColor: Option[String] = {
    val topCard = shuffledCardStack.head
    /*topCard match {
      case card: DefaultCard => Some(card.color)
      case _ => None
    }*/
    Card.getType(topCard)
  }

  def stitchInThisCycle: RoundManager = {
    val stitchPlayer = Card.getPlayerOfHighestCard(playedCards.reverse, trumpColor)
    //val stitchPlayer = CardStack.getPlayerOfHighestCard(playedCards.reverse, trumpColor)
    val mutMap = collection.mutable.Map() ++ stitchesPerRound
    mutMap.put(stitchPlayer.name, mutMap(stitchPlayer.name) + 1)
    this.copy(stitchesPerRound = mutMap.toMap, playedCards = Nil)
  }

  def pointsForRound(): ResultTable = {
    var table = resultTable
    for (i <- players.indices) {
      table = table.updatePoints(currentRound, i,
        RoundManager.calcPoints(predictionPerRound(i), stitchesPerRound(players(i).name)))
    }

    table
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
      RoundManager(
        numberOfPlayers, numberOfRounds,
        resultTable = ResultTable(numberOfRounds, numberOfPlayers, ResultTable.initializeVector(numberOfRounds, numberOfPlayers))
      )
    }
  }
}