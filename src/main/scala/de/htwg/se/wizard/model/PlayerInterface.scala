package de.htwg.se.wizard.model

trait PlayerInterface {
  def getPlayerCards: Option[List[CardInterface]]

  def assignCards(cards: Option[List[CardInterface]]): PlayerInterface

  def getName: String

  def toXML: String
}

trait StaticPlayerInterface {

  def newPlayer(name: String): PlayerInterface

  def checkNumberOfPlayers(number: Int): Boolean

  def playerPrediction(player: PlayerInterface, round: Int, trump: Option[String]): String

  def playerTurn(player: PlayerInterface, round: Int): String
}

