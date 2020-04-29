package de.htwg.se.wizard.controller.controllerComponent

import de.htwg.se.wizard.util.Observable

trait ControllerInterface extends Observable {
  def undo(): Unit

  def redo(): Unit

  def eval(input: String): Unit

  def save(): Unit

  def load(): Unit

  // TODO: Mehr Scala Style
  def getCurrentStateAsString: String

  def controllerStateAsString: String

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
}
