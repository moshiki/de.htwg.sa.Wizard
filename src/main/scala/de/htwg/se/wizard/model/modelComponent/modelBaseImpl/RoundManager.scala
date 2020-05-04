package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards.{Card, CardStack, DefaultCard}
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable.ListBuffer
import scala.xml.Elem

case class RoundManager(numberOfPlayers: Int = 0,
                        numberOfRounds: Int = 0,
                        shuffledCardStack: List[Card] = CardStack.shuffleCards(CardStack.initialize),
                        players: List[Player] = Nil,
                        currentPlayer: Int = 0,
                        currentRound: Int = 1,
                        predictionPerRound: List[Int] = Nil,
                        tricksPerRound: Map[String, Int] = Map.empty[String, Int],
                        playedCards: List[Card] = Nil,
                        predictionMode: Boolean = true,
                        cleanMap: Map[String, Int] = Map.empty[String, Int],
                        resultTable: ResultTable) extends ModelInterface {
  val initialCardStack: List[Card] = CardStack.initialize

  override def isNumberOfPlayersValid(number: Int): Boolean = {
    Player.checkNumberOfPlayers(number)
  }

  override def addPlayer(name: String): RoundManager = {
    val newPlayer = Player(name)
    val oldPlayerList = this.players
    if (oldPlayerList contains newPlayer) return this
    val newPlayerList = oldPlayerList ::: List(newPlayer)
    val newTricksPerRound = collection.mutable.Map() ++ tricksPerRound += name -> 0
    copy(players = newPlayerList, tricksPerRound = newTricksPerRound.toMap)
  }

  override def playCard(selectedCard: Int): RoundManager = {
    val player = players(currentPlayer)
    val currentPlayersCards = player.getPlayerCards.get.to(ListBuffer)
    val playedCard = currentPlayersCards.remove(selectedCard - 1)
    val newPlayers = players to ListBuffer
    newPlayers.update(currentPlayer, player.assignCards(Some(currentPlayersCards.toList)))
    copy(playedCards = playedCard :: playedCards, players = newPlayers.toList)
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
    copy(shuffledCardStack = stack.toList, players = newPlayers.toList)
  }

  override def updatePlayerPrediction(input: Int): RoundManager = copy(predictionPerRound = predictionPerRound ::: List(input))

  override def setupStrings: String = "Player " + currentPlayer + ", please enter your name:"

  def nextPlayerSetup: Int = if (currentPlayer < numberOfPlayers) currentPlayer + 1 else 0

  override def playerStateStrings: String = {
    if (currentRound == numberOfRounds && currentPlayer == 0) {
      return "\nGame Over! Press 'q' to quit.\n" + resultTable.toString
    }
    if (predictionPerRound.size < numberOfPlayers) { // TODO
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
      copy(
        resultTable = pointsForRound(),
        shuffledCardStack = CardStack.shuffleCards(initialCardStack),
        predictionPerRound = Nil,
        tricksPerRound = cleanMap,
        currentRound = currentRound + 1
      )
    }
    else this
  }


  override def nextPlayer: RoundManager = {
    if (currentPlayer < numberOfPlayers - 1) copy(currentPlayer = currentPlayer + 1)
    else {
      var newRoundManager = this
      if (!predictionMode) newRoundManager = trickInThisCycle
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

  def trickInThisCycle: RoundManager = {
    val trickPlayer = CardStack.getPlayerOfHighestCard(playedCards.reverse, trumpColor)
    val mutMap = collection.mutable.Map() ++ tricksPerRound
    mutMap.put(trickPlayer.getName, mutMap(trickPlayer.getName) + 1)
    this.copy(tricksPerRound = mutMap.toMap, playedCards = Nil)
  }

  def pointsForRound(): ResultTable = {
    var table = resultTable
    for (i <- players.indices) { // TODO: Eventuell Datenstruktur anpassen
      table = table.updatePoints(currentRound, i,
        RoundManager.calcPoints(predictionPerRound(i), tricksPerRound(players(i).getName)))
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
      <tricksPerRound>{mapToXMLList(tricksPerRound)}</tricksPerRound>
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
    val tricksPerRoundNode = (node \ "tricksPerRound") \ "entry"
    var tricksPerRound = Map.empty[String, Int]
    tricksPerRoundNode.reverse.foreach(node => tricksPerRound = tricksPerRound + ((node \ "player").text -> (node \ "trick").text.toInt))
    val playedCardsNode = (node \ "playedCards").head.child
    val playedCards = playedCardsNode.map(node => Card.fromXML(node))
    val predictionMode = (node \ "predictionMode").text.toBoolean
    val cleanMapNode = (node \ "cleanMap") \ "entry"
    var cleanMap = Map.empty[String, Int]
    cleanMapNode.reverse.foreach(node => cleanMap = cleanMap + ((node \ "player").text -> (node \ "trick").text.toInt))
    val resultTable = this.resultTable.fromXML((node \ "resultTable").head.child.head)
    copy(
      numberOfPlayers = numberOfPlayers,
      numberOfRounds = numberOfRounds,
      shuffledCardStack = shuffledCardStack.toList,
      players = players.toList,
      currentPlayer = currentPlayer,
      currentRound = currentRound,
      predictionPerRound = predictionPerRound.toList,
      tricksPerRound = tricksPerRound,
      playedCards = playedCards.toList,
      predictionMode = predictionMode,
      cleanMap = cleanMap,
      resultTable = resultTable
    )
  }

  override def currentPlayerNumber: Int = currentPlayer

  override def currentPlayerString: String = players(currentPlayer).toString

  override def currentAmountOfTricks: Int = tricksPerRound(currentPlayerString)

  override def playerPrediction: Int = predictionPerRound(currentPlayerNumber)

  override def playedCardsAsString: List[String] = playedCards.map(card => card.toString)

  override def currentPlayersCards: List[String] = players(currentPlayerNumber).getPlayerCards.get.map(card => card.toString)

  override def topOfStackCardString: String = shuffledCardStack.head.toString

  override def playersAsStringList: List[String] = players.map(player => player.toString)

  override def resultArray: Array[Array[Any]] = resultTable.toAnyArray

  override def nextPlayerInSetup: ModelInterface = this.copy(currentPlayer = nextPlayerSetup)

  override def createdPlayers: Int = players.size

  override def saveCleanMap: ModelInterface = this.copy(cleanMap = tricksPerRound)

  override def invokePredictionMode(): ModelInterface = this.copy(predictionMode = true)

  override def recordedPredictions: Int = predictionPerRound.size

  override def leavePredictionMode: ModelInterface = this copy(predictionMode = false)

  override def configurePlayersAndRounds(numberOfPlayer: Int): ModelInterface = RoundStrategy.execute(numberOfPlayer)

  override def toJson: JsValue = Json.toJson(this)

  override def fromJson(jsValue: JsValue): RoundManager = jsValue.validate[RoundManager].asOpt.get
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