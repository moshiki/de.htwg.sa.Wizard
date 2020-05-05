package de.htwg.se.wizard.controller.controllerComponent

import de.htwg.se.wizard.util.Observable

trait ControllerInterface extends Observable {
  def undo(): Unit

  def redo(): Unit

  def eval(input: String): Unit

  def save(): Unit

  def load(): Unit

  def currentStateAsString: String

  def controllerStateAsString: String

  def currentPlayerNumber: Int

  def currentPlayerString: String

  def currentAmountOfStitches: Int

  def playerPrediction: Int

  def predictionMode: Boolean

  def currentRound: Int

  def playedCardsAsString: List[String]

  def currentPlayersCards: List[String]

  def topOfStackCardString: String

  def playersAsStringList: List[String]

  def resultArray: Array[Array[Any]]
}
