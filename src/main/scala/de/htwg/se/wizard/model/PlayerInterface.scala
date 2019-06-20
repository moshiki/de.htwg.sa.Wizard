package de.htwg.se.wizard.model

trait PlayerInterface {

  def newPlayer(name: String): SpecificPlayerInterface

  def checkNumberOfPlayers(number: Int): Boolean

  def playerPrediction(player: SpecificPlayerInterface, round: Int, trump: Option[String]): String

  def playerTurn(player: SpecificPlayerInterface, round: Int): String

}

trait SpecificPlayerInterface {
  def getPlayerCards: Option[List[SpecificCardInterface]]

  def assignCards(cards: Option[List[SpecificCardInterface]]): SpecificPlayerInterface

  def getName: String
}
