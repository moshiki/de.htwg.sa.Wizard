package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards.{Card, CardStack, DefaultCard}
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable.ListBuffer
import scala.xml.Elem

// TODO: Getter und mehr Scala-Style
case class RoundManager(numberOfPlayers: Int = 0,
                        numberOfRounds: Int = 0,
                        shuffledCardStack: List[Card] = CardStack.shuffleCards(CardStack.initialize),
                        players: List[Player] = Nil,
                        currentPlayer: Int = 0,
                        currentRound: Int = 1,
                        predictionPerRound: List[Int] = Nil,
                        stitchesPerRound: Map[String, Int] = Map.empty[String, Int],
                        playedCards: List[Card] = Nil,
                        predictionMode: Boolean = true,
                        cleanMap: Map[String, Int] = Map.empty[String, Int],
                        resultTable: ResultTable) extends ModelInterface {
  val initialCardStack: List[Card] = CardStack.initialize

  override def checkNumberOfPlayers(number: Int): Boolean = {
    Player.checkNumberOfPlayers(number)
  }

  override def addPlayer(name: String): RoundManager = {

    val newPlayer = Player(name)
    val oldPlayerList = this.players
    if (oldPlayerList contains newPlayer) return this
    val newPlayerList = oldPlayerList ::: List(newPlayer)

    var newStitchesPerRound = collection.mutable.Map() ++ stitchesPerRound
    newStitchesPerRound += name -> 0

    this.copy(players = newPlayerList, stitchesPerRound = newStitchesPerRound.toMap)
  }

  override def playCard(selectedCard: Int): RoundManager = {
    val player = players(currentPlayer)
    val currentPlayersCards = player.getPlayerCards.get.to(ListBuffer)
    val playedCard = currentPlayersCards.remove(selectedCard - 1)

    val newPlayers = players to ListBuffer
    newPlayers.update(currentPlayer, player.assignCards(Some(currentPlayersCards.toList)))

    this.copy(playedCards = playedCard :: playedCards, players = newPlayers.toList)
  }


  override def cardDistribution: RoundManager = {
    if (players.head.getPlayerCards.isDefined && players.head.getPlayerCards.get.nonEmpty) return this
    val stack = shuffledCardStack to ListBuffer
    val newPlayers = players to ListBuffer

    for (i <- players.indices) { // TODO: Rekursiv
      var list = List[Card]()
      for (_ <- 1 to currentRound) {
        val card = stack.remove(0)
        val typ = Card.setOwner(card, players(i))
        list = list ::: List[Card](typ)
      }

      val newPlayer = players(i).assignCards(Some(list))
      newPlayers.update(i, newPlayer)
    }

    this.copy(shuffledCardStack = stack.toList, players = newPlayers.toList)
  }

  override def updatePlayerPrediction(input: Int): RoundManager = {
    this.copy(predictionPerRound = predictionPerRound ::: List(input))
  }

  override def getSetupStrings: String = {
    "Player " + currentPlayer + ", please enter your name:"
  }

  def nextPlayerSetup: Int = if (currentPlayer < numberOfPlayers) currentPlayer + 1 else 0

  override def getPlayerStateStrings: String = {
    if (currentRound == numberOfRounds && currentPlayer == 0) {
      return "\nGame Over! Press 'q' to quit.\n" + resultTable.toString
    }
    if (predictionPerRound.size < numberOfPlayers) {
      var out = "\n"
      if (currentPlayer == 0) out += resultTable.toString + "\n"
      out += Player.playerPrediction(players(currentPlayer), currentRound, trumpColor)
      out
    } else {
      Player.playerTurn(players(currentPlayer), currentRound)
    }
  }

  override def nextRound: RoundManager = {
    if (currentPlayer == 0 && currentRound != numberOfRounds && players.last.getPlayerCards.get.isEmpty) {
      this.copy(
        resultTable = pointsForRound(),
        shuffledCardStack = CardStack.shuffleCards(initialCardStack),
        predictionPerRound = Nil,
        stitchesPerRound = cleanMap,
        currentRound = currentRound + 1
      )
    }
    else this
  }


  override def nextPlayer: RoundManager = {
    if (currentPlayer < numberOfPlayers - 1) copy(currentPlayer = currentPlayer + 1)
    else {
      var newRoundManager = this
      if (!predictionMode) newRoundManager = stitchInThisCycle
      newRoundManager.copy(currentPlayer = 0)
    }
  }


  def trumpColor: Option[String] = {
    val topCard = shuffledCardStack.head
    topCard match {
      case card: DefaultCard => Some(card.color)
      case _ => None
    }
  }

  def stitchInThisCycle: RoundManager = {
    val stitchPlayer = CardStack.getPlayerOfHighestCard(playedCards.reverse, trumpColor)
    //val stitchPlayer = CardStack.getPlayerOfHighestCard(playedCards.reverse, trumpColor)
    val mutMap = collection.mutable.Map() ++ stitchesPerRound
    mutMap.put(stitchPlayer.getName, mutMap(stitchPlayer.getName) + 1)
    this.copy(stitchesPerRound = mutMap.toMap, playedCards = Nil)
  }

  def pointsForRound(): ResultTable = {
    var table = resultTable
    for (i <- players.indices) { // TODO: Eventuell Datenstruktur anpassen
      table = table.updatePoints(currentRound, i,
        RoundManager.calcPoints(predictionPerRound(i), stitchesPerRound(players(i).getName)))
    }

    table
  }

  def mapToXMLList(map: Map[String, Int]): List[Elem] = {
    var list = List.empty[Elem]
    map.foreach(kv => list = <entry><player>{kv._1}</player><trick>{kv._2}</trick></entry> :: list)
    list
  }

  override def toXML: Elem = {
    <RoundManager>
      <numberOfPlayers>{numberOfPlayers}</numberOfPlayers>
      <numberOfRounds>{numberOfRounds}</numberOfRounds>
      <shuffledCardStack>{shuffledCardStack map(card => card.toXML)}</shuffledCardStack>
      <players>{players map(player => player.toXML)}</players>
      <currentPlayer>{currentPlayer}</currentPlayer>
      <currentRound>{currentRound}</currentRound>
      <predictionPerRound>{for (i <- predictionPerRound.indices) yield <prediction>{predictionPerRound(i)}</prediction>}</predictionPerRound>
      <stitchesPerRound>{mapToXMLList(stitchesPerRound)}</stitchesPerRound>
      <playedCards>{playedCards.map(card => card.toXML)}</playedCards>
      <predictionMode>{predictionMode}</predictionMode>
      <cleanMap>{mapToXMLList(cleanMap)}</cleanMap>
      <resultTable>{resultTable.toXML}</resultTable>
    </RoundManager>
  }

  override def fromXML(node: scala.xml.Node): RoundManager = {
    val numberOfPlayers = (node \ "numberOfPlayers").text.toInt
    val numberOfRounds = (node \ "numberOfRounds").text.toInt

    val shuffledCardStackNode = (node \ "shuffledCardStack").head.child
    val shuffledCardStack = shuffledCardStackNode.map(node => Card.fromXML(node))

    val playersNode = (node \ "players").head.child
    val players = playersNode.map(node => Player.fromXML(node))

    val currentPlayer = (node \ "currentPlayer").text.toInt
    val currentRound = (node \ "currentRound").text.toInt

    val predictionPerRoundNode = (node \ "predictionPerRound").head.child
    val predictionPerRound = predictionPerRoundNode.map(node => (node \\ "prediction").text.toInt)

    val stitchesPerRoundNode = (node \ "stitchesPerRound") \ "entry"
    var stitchesPerRound = Map.empty[String, Int]
    stitchesPerRoundNode.reverse.foreach(node => stitchesPerRound = stitchesPerRound + ((node \ "player").text -> (node \ "trick").text.toInt))

    val playedCardsNode = (node \ "playedCards").head.child
    val playedCards = playedCardsNode.map(node => Card.fromXML(node))

    val predictionMode = (node \ "predictionMode").text.toBoolean

    val cleanMapNode = (node \ "cleanMap") \ "entry"
    var cleanMap = Map.empty[String, Int]
    cleanMapNode.reverse.foreach(node => cleanMap = cleanMap + ((node \ "player").text -> (node \ "trick").text.toInt))

    val resultTable = this.resultTable.fromXML((node \ "resultTable").head.child.head)

    this.copy(
      numberOfPlayers = numberOfPlayers,
      numberOfRounds = numberOfRounds,
      shuffledCardStack = shuffledCardStack.toList,
      players = players.toList,
      currentPlayer = currentPlayer,
      currentRound = currentRound,
      predictionPerRound = predictionPerRound.toList,
      stitchesPerRound = stitchesPerRound,
      playedCards = playedCards.toList,
      predictionMode = predictionMode,
      cleanMap = cleanMap,
      resultTable = resultTable
    )
  }

  override def getCurrentPlayerNumber: Int = currentPlayer

  override def getCurrentPlayerString: String = players(currentPlayer).toString

  override def getCurrentAmountOfStitches: Int = stitchesPerRound(getCurrentPlayerString)

  override def getPlayerPrediction: Int = predictionPerRound(getCurrentPlayerNumber)

  override def playedCardsAsString: List[String] = playedCards.map(card => card.toString)

  override def currentPlayersCards: List[String] = players(getCurrentPlayerNumber).getPlayerCards.get.map(card => card.toString)

  override def topOfStackCardString: String = shuffledCardStack.head.toString

  override def playersAsStringList: List[String] = players.map(player => player.toString)

  override def resultArray: Array[Array[Any]] = resultTable.toAnyArray

  override def nextPlayerInSetup: ModelInterface = this.copy(currentPlayer = nextPlayerSetup)

  override def createdPlayers: Int = players.size

  override def saveCleanMap: ModelInterface = this.copy(cleanMap = stitchesPerRound)

  override def setPredictionMode(): ModelInterface = this.copy(predictionMode = true)

  override def recordedPredictions: Int = predictionPerRound.size

  override def unsetPredictionMode: ModelInterface = this copy(predictionMode = false)

  override def setPlayersAndRounds(numberOfPlayer: Int): ModelInterface = RoundStrategy.execute(numberOfPlayer)

  override def toJson: JsValue = Json.toJson(this)

  override def fromJson(jsValue: JsValue): RoundManager = jsValue.validate[RoundManager].asOpt.get
}

object RoundManager {
  def calcPoints(playerPrediction: Int, stitches: Int): Int = {
    var points = 0
    for (_ <- 1 to stitches) points += 10
    if (playerPrediction == stitches) {
      points += 20
    }
    if (playerPrediction < stitches) for (_ <- playerPrediction until stitches) {
      points -= 10
    }
    if (playerPrediction > stitches) for (_ <- stitches until playerPrediction) {
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

    def build(): RoundManager = {
      RoundManager(
        numberOfPlayers, numberOfRounds,
        resultTable = ResultTable.initializeTable(numberOfRounds, numberOfPlayers))
    }
  }

  import play.api.libs.json._
  implicit val roundManagerWrites: OWrites[RoundManager] = Json.writes[RoundManager]
  implicit val roundManagerReads: Reads[RoundManager] = Json.reads[RoundManager]
}