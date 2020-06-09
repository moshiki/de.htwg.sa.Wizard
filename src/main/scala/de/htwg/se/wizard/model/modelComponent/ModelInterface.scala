package de.htwg.se.wizard.model.modelComponent

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface
import play.api.libs.json.JsValue

import scala.xml.Elem

trait ModelInterface {
  def toXML: Elem

  def fromXML(node: scala.xml.Node): ModelInterface

  def currentPlayerNumber: Int

  def currentPlayerString: String

  def currentAmountOfTricks: Int

  def playerPrediction: Int

  def predictionMode: Boolean

  def currentRound: Int

  def playedCardsAsString: List[String]

  def currentPlayersCards: List[String]

  def topOfStackCardString: String

  def playersAsStringList: List[String]

  def isNumberOfPlayersValid(number: Int): Boolean

  def nextPlayerInSetup: ModelInterface

  def addPlayer(name: String): ModelInterface

  def createdPlayers: Int

  def numberOfPlayers: Int

  def saveCleanMap: ModelInterface

  def invokePredictionMode(): ModelInterface

  def cardDistribution: ModelInterface

  def setupStrings: String

  def updatePlayerPrediction(input: Int): ModelInterface

  def playCard(selectedCard: Int): ModelInterface

  def nextPlayer: ModelInterface

  def nextRound: ModelInterface

  def numberOfRounds: Int

  def recordedPredictions: Int

  def leavePredictionMode: ModelInterface

  def playerStateStrings: String

  def configurePlayersAndRounds(numberOfPlayer: Int): ModelInterface

  def toJson: JsValue

  def fromJson(jsValue: JsValue): ModelInterface

  def isTimeForNextRound: Boolean

  def pointsForThisRound: Vector[Int]

  def players: List[Player]

  def predictionPerRound: List[Int]

  def tricksPerRound: Map[String, Int]

  def playedCards: List[CardInterface]
}
