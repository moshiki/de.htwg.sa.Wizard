package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface
import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface._
import de.htwg.sa.wizard.cardModule.util._
import de.htwg.se.wizard.model.modelComponent.{ModelInterface, Player}
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success, Try}
import scala.xml.Elem

case class RoundManager(numberOfPlayers: Int = 0,
                        numberOfRounds: Int = 0,
                        players: List[Player] = Nil,
                        currentPlayerNumber: Int = 0,
                        currentRound: Int = 1,
                        predictionPerRound: List[Int] = Nil,
                        tricksPerRound: Map[String, Int] = Map.empty[String, Int],
                        playedCards: List[CardInterface] = Nil,
                        predictionMode: Boolean = true,
                        cleanMap: Map[String, Int] = Map.empty[String, Int]) extends ModelInterface with PlayJsonSupport {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val cardModuleHost: String = "http://" + sys.env.getOrElse("CARDMODULE_HOST", "localhost:1234") +"/"

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
      val cardsForPlayer = {
        val response = Http().singleRequest(Post(cardModuleHost + "cardStack/cardsForPlayer",
          CardsForPlayerArgumentContainer(playerNumber, currentRound)))
        val jsonStringFuture = response.flatMap(r => Unmarshal(r.entity).to[List[CardInterface]])
        Await.result(jsonStringFuture, Duration(5, TimeUnit.SECONDS))
      }
      val assignedCards = {
        val response = Http().singleRequest(Post(cardModuleHost + "cardStack/assignCardsToPlayer",
          AssignCardsToPlayerArgumentContainer(cardsForPlayer, player.name)))
        val jsonStringFuture = response.flatMap(r => Unmarshal(r.entity).to[List[CardInterface]])
        Await.result(jsonStringFuture, Duration(1, TimeUnit.SECONDS))
      }
      player.assignCards(assignedCards)
    })
    Http().singleRequest(Post(cardModuleHost + "cardStack/splitCardStack",
      SplitCardStackArgumentContainer(numberOfPlayers, currentRound)))
    copy(players = playersWithCards)
  }

  override def updatePlayerPrediction(input: Int): RoundManager = copy(predictionPerRound = predictionPerRound ::: List(input))

  override def setupStrings: String = "Player " + currentPlayerNumber + ", please enter your name:"

  def nextPlayerSetup: Int = if (currentPlayerNumber < numberOfPlayers) currentPlayerNumber + 1 else 0

  override def playerStateStrings: String = {
    if (currentRound == numberOfRounds && currentPlayerNumber == 0) {
      return "\nGame Over! Press 'q' to quit.\n"
    }
    if (predictionPerRound.size < numberOfPlayers) {
      val trumpColorFuture = Http().singleRequest(HttpRequest(uri = cardModuleHost + "cardStack/trumpColor"))
      val jsonStringFuture = trumpColorFuture.flatMap(r => Unmarshal(r.entity).to[Option[String]])
      val trumpColor = Await.result(jsonStringFuture, Duration(1, TimeUnit.SECONDS))
      Player.playerPrediction(players(currentPlayerNumber), currentRound, trumpColor)
    } else {
      Player.playerTurn(players(currentPlayerNumber), currentRound)
    }
  }

  override def nextRound: RoundManager = {
    if (currentPlayerNumber == 0 && currentRound != numberOfRounds && players.last.playerCards.isEmpty) {
      Http().singleRequest(HttpRequest(uri = cardModuleHost + "cardStack/shuffleCardStack"))
      copy(
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

  def trickInThisCycle: RoundManager = {

      val response = Http().singleRequest(Post(cardModuleHost + "cardStack/playerOfHighestCard", playedCards.reverse))
    var futurestring: Future[String]= Future("")
    var trickPlayerName = ""
    Try {
      futurestring = response.flatMap(r => Unmarshal(r.entity).to[String])
      trickPlayerName = Await.result(futurestring, Duration(1, TimeUnit.SECONDS))
    } match {
      case Success(_) => println("SUCCESS!!"); println(futurestring)
      case Failure(exception) => println(exception.getMessage); println(response)
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

  override def topOfStackCardString: String = {
    val response = Http().singleRequest(Get(cardModuleHost + "cardStack/topOfCardStackString"))
    val topOfStackStringFuture = response.flatMap(r => Unmarshal(r.entity).to[String])
    Await.result(topOfStackStringFuture, Duration(1, TimeUnit.SECONDS))
  }

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

  override def buildModel(numberOfPlayers: Int, numberOfRounds: Int, players: List[Player], currentPlayerNumber: Int, currentRound: Int, predictionPerRound: List[Int], tricksPerRound: Map[String, Int], playedCards: List[CardInterface], predictionMode: Boolean): ModelInterface =
    copy(numberOfPlayers, numberOfRounds, players, currentPlayerNumber, currentRound, predictionPerRound, tricksPerRound, playedCards, predictionMode)
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