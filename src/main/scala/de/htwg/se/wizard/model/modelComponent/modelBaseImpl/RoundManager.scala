package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import de.htwg.sa.wizard.cardModule.model.cardComponent.{CardInterface, CardStackInterface}
import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.DefaultCard
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable.ListBuffer
import scala.xml.Elem

case class RoundManager(numberOfPlayers: Int = 0,
                        numberOfRounds: Int = 0,
                        shuffledCardStack: List[CardInterface] = CardStack.shuffleCards(CardStack.initialize),
                        players: List[Player] = Nil,
                        currentPlayerNumber: Int = 0,
                        currentRound: Int = 1,
                        predictionPerRound: List[Int] = Nil,
                        tricksPerRound: Map[String, Int] = Map.empty[String, Int],
                        playedCards: List[CardInterface] = Nil,
                        predictionMode: Boolean = true,
                        cleanMap: Map[String, Int] = Map.empty[String, Int]) extends ModelInterface {
  val initialCardStack: List[CardInterface] = CardStack.initialize

  override def isNumberOfPlayersValid(number: Int): Boolean = Player.checkNumberOfPlayers(number)

  override def addPlayer(name: String): RoundManager = {
    val newPlayer = Player(name)
    val oldPlayerList = this.players
    if (oldPlayerList contains newPlayer) return this
    val newPlayerList = oldPlayerList ::: List(newPlayer)
    val newTricksPerRound = collection.mutable.Map() ++ tricksPerRound += name -> 0
    copy(players = newPlayerList, tricksPerRound = newTricksPerRound.toMap)
  }

  override def playCard(selectedCard: Int): RoundManager = {
    val player = players(currentPlayerNumber)
    val currentPlayersCards = player.playerCards.to(ListBuffer)
    val playedCard = currentPlayersCards.remove(selectedCard - 1)
    val newPlayers = players to ListBuffer
    newPlayers.update(currentPlayerNumber, player.assignCards(currentPlayersCards.toList))
    copy(playedCards = playedCard :: playedCards, players = newPlayers.toList)
  }


  override def cardDistribution: RoundManager = {
    if (players.head.playerCards.nonEmpty) return this
    val playersWithCards = players map(player => {
      val playerNumber = players.indexOf(player)
      val cardsForPlayer = shuffledCardStack.slice(playerNumber * currentRound, playerNumber * currentRound + currentRound)
      val assignedCards = cardsForPlayer.map(card => card.setOwner(player.name))
      player.assignCards(assignedCards)
    })
    val newShuffledCardStack = shuffledCardStack.splitAt((numberOfPlayers - 1) * currentRound + 1)._2
    copy(shuffledCardStack = newShuffledCardStack, players = playersWithCards)
  }

  override def updatePlayerPrediction(input: Int): RoundManager = copy(predictionPerRound = predictionPerRound ::: List(input))

  override def setupStrings: String = "Player " + currentPlayerNumber + ", please enter your name:"

  def nextPlayerSetup: Int = if (currentPlayerNumber < numberOfPlayers) currentPlayerNumber + 1 else 0

  override def playerStateStrings: String = {
    if (currentRound == numberOfRounds && currentPlayerNumber == 0) {
      return "\nGame Over! Press 'q' to quit.\n"
    }
    if (predictionPerRound.size < numberOfPlayers) {
      Player.playerPrediction(players(currentPlayerNumber), currentRound, trumpColor)
    } else {
      Player.playerTurn(players(currentPlayerNumber), currentRound)
    }
  }

  override def nextRound: RoundManager = {
    if (currentPlayerNumber == 0 && currentRound != numberOfRounds && players.last.playerCards.isEmpty) {
      copy(
        shuffledCardStack = CardStack.shuffleCards(initialCardStack),
        predictionPerRound = Nil,
        tricksPerRound = cleanMap,
        currentRound = currentRound + 1
      )
    }
    else this
  }

  override def nextPlayer: RoundManager = {
    if (currentPlayerNumber < numberOfPlayers - 1) copy(currentPlayerNumber = currentPlayerNumber + 1)
    else {
      val newRoundManager = if (!predictionMode) trickInThisCycle else this
      newRoundManager.copy(currentPlayerNumber = 0)
    }
  }

  def trumpColor: Option[String] = {
    val topCard = shuffledCardStack.head
    topCard match {
      case card: DefaultCard => Some(card.color)
      case _ => None
    }
  }

  def trickInThisCycle: RoundManager = {
    val trickPlayerName = CardStack.playerOfHighestCard(playedCards.reverse, trumpColor) match {
      case Some(playerName) => playerName
      // TODO: case None => Exception?
    }
    val mutMap = collection.mutable.Map() ++ tricksPerRound
    mutMap.put(trickPlayerName, mutMap(trickPlayerName) + 1)
    this.copy(tricksPerRound = mutMap.toMap, playedCards = Nil)
  }

  def mapToXMLList(map: Map[String, Int]): List[Elem] = map.map(kv => <entry><player>{kv._1}</player><trick>{kv._2}</trick></entry>).toList

  override def toXML: Elem = {
    <RoundManager>
      <numberOfPlayers>{numberOfPlayers}</numberOfPlayers>
      <numberOfRounds>{numberOfRounds}</numberOfRounds>
      <shuffledCardStack>{shuffledCardStack map(card => card.toXML)}</shuffledCardStack>
      <players>{players map(player => player.toXML)}</players>
      <currentPlayer>{currentPlayerNumber}</currentPlayer>
      <currentRound>{currentRound}</currentRound>
      <predictionPerRound>{predictionPerRound.map(prediction => <prediction>{prediction}</prediction>)}</predictionPerRound>
      <tricksPerRound>{mapToXMLList(tricksPerRound)}</tricksPerRound>
      <playedCards>{playedCards.map(card => card.toXML)}</playedCards>
      <predictionMode>{predictionMode}</predictionMode>
      <cleanMap>{mapToXMLList(cleanMap)}</cleanMap>
    </RoundManager>
  }

  override def fromXML(node: scala.xml.Node): RoundManager = {
    val numberOfPlayers = (node \ "numberOfPlayers").text.toInt
    val numberOfRounds = (node \ "numberOfRounds").text.toInt
    val shuffledCardStackNode = (node \ "shuffledCardStack").head.child
    val shuffledCardStack = shuffledCardStackNode.map(node => CardInterface(node.label).fromXML(node))
    val playersNode = (node \ "players").head.child
    val players = playersNode.map(node => Player.fromXML(node))
    val currentPlayer = (node \ "currentPlayer").text.toInt
    val currentRound = (node \ "currentRound").text.toInt
    val predictionPerRoundNode = (node \ "predictionPerRound").head.child
    val predictionPerRound = predictionPerRoundNode.map(node => (node \\ "prediction").text.toInt)
    val tricksPerRoundNode = (node \ "tricksPerRound") \ "entry"
    val tricksPerRound = tricksPerRoundNode.reverse.map(node => (node \ "player").text -> (node \ "trick").text.toInt).toMap
    val playedCardsNode = (node \ "playedCards").head.child
    val playedCards = playedCardsNode.map(node => CardInterface(node.label).fromXML(node))
    val predictionMode = (node \ "predictionMode").text.toBoolean
    val cleanMapNode = (node \ "cleanMap") \ "entry"
    val cleanMap = cleanMapNode.reverse.map(node => (node \ "player").text -> (node \ "trick").text.toInt).toMap
    copy(
      numberOfPlayers = numberOfPlayers,
      numberOfRounds = numberOfRounds,
      shuffledCardStack = shuffledCardStack.toList,
      players = players.toList,
      currentPlayerNumber = currentPlayer,
      currentRound = currentRound,
      predictionPerRound = predictionPerRound.toList,
      tricksPerRound = tricksPerRound,
      playedCards = playedCards.toList,
      predictionMode = predictionMode,
      cleanMap = cleanMap
    )
  }

  override def currentPlayerString: String = players(currentPlayerNumber).toString

  override def currentAmountOfTricks: Int = tricksPerRound(currentPlayerString)

  override def playerPrediction: Int = predictionPerRound(currentPlayerNumber)

  override def playedCardsAsString: List[String] = playedCards.map(card => card.toString)

  override def currentPlayersCards: List[String] = players(currentPlayerNumber).playerCards.map(card => card.toString)

  override def topOfStackCardString: String = shuffledCardStack.head.toString

  override def playersAsStringList: List[String] = players.map(player => player.toString)

  override def nextPlayerInSetup: ModelInterface = this.copy(currentPlayerNumber = nextPlayerSetup)

  override def createdPlayers: Int = players.size

  override def saveCleanMap: ModelInterface = this.copy(cleanMap = tricksPerRound)

  override def invokePredictionMode(): ModelInterface = this.copy(predictionMode = true)

  override def recordedPredictions: Int = predictionPerRound.size

  override def leavePredictionMode: ModelInterface = this copy(predictionMode = false)

  override def configurePlayersAndRounds(numberOfPlayer: Int): ModelInterface = RoundStrategy.execute(numberOfPlayer)

  override def toJson: JsValue = Json.toJson(this)

  override def fromJson(jsValue: JsValue): RoundManager = jsValue.validate[RoundManager].get

  override def isTimeForNextRound: Boolean = !predictionMode && currentPlayerNumber == 0 && currentRound != numberOfRounds && players.last.playerCards.isEmpty

  override def pointsForThisRound: Vector[Int] = players.map(player => {
    val playerIndex = players.indexOf(player)
    RoundManager.calcPoints(predictionPerRound(playerIndex), tricksPerRound(player.name))}).toVector
}

object RoundManager {
  def calcPoints(playerPrediction: Int, tricks: Int): Int = {
    var points = 0
    for (_ <- 1 to tricks) points += 10
    if (playerPrediction == tricks) {
      points += 20
    }
    if (playerPrediction < tricks) for (_ <- playerPrediction until tricks) {
      points -= 10
    }
    if (playerPrediction > tricks) for (_ <- tricks until playerPrediction) {
      points -= 10
    }
    points
  }


  case class Builder() {
    var numberOfPlayers: Int = 0
    var numberOfRounds: Int = 0

    def withNumberOfPlayers(players: Int): Builder = {
      numberOfPlayers = players
      this
    }

    def withNumberOfRounds(rounds: Int): Builder = {
      numberOfRounds = rounds
      this
    }

    def build(): RoundManager = RoundManager(numberOfPlayers, numberOfRounds)
  }

  import play.api.libs.json._
  implicit val roundManagerWrites: OWrites[RoundManager] = Json.writes[RoundManager]
  implicit val roundManagerReads: Reads[RoundManager] = Json.reads[RoundManager]
}