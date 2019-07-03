package de.htwg.se.wizard.model.modelComponent

import play.api.libs.json.JsValue

import scala.xml.Elem

trait ModelInterface {
  def toXML: Elem

  def fromXML(node: scala.xml.Node): ModelInterface

  def getCurrentPlayerNumber: Int

  def getCurrentPlayerString: String

  def getCurrentAmountOfStitches: Int

  def getPlayerPrediction: Int

  def predictionMode: Boolean

  def currentRound: Int

  def playedCardsAsString: List[String]

  def currentPlayersCards: List[String]

  def topOfStackCardString: String

  def playersAsStringList: List[String]

  def resultArray: Array[Array[Any]]

  def checkNumberOfPlayers(number: Int): Boolean

  def nextPlayerInSetup: ModelInterface

  def addPlayer(name: String): ModelInterface

  def createdPlayers: Int

  def numberOfPlayers: Int

  def saveCleanMap: ModelInterface

  def setPredictionMode: ModelInterface

  def cardDistribution: ModelInterface

  def getSetupStrings: String

  def updatePlayerPrediction(input: Int): ModelInterface

  def playCard(selectedCard: Int): ModelInterface

  def nextPlayer: ModelInterface

  def nextRound: ModelInterface

  def numberOfRounds: Int

  def recordedPredictions: Int

  def unsetPredictionMode: ModelInterface

  def getPlayerStateStrings: String

  def setPlayersAndRounds(numberOfPlayer: Int): ModelInterface

  def toJson: JsValue
}
