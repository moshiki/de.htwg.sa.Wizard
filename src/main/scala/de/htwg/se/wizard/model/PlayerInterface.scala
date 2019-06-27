package de.htwg.se.wizard.model

import scala.xml.Elem

trait PlayerInterface {
  def getPlayerCards: Option[List[CardInterface]]

  def assignCards(cards: Option[List[CardInterface]]): PlayerInterface

  def getName: String

  def toXML: Elem
}

trait StaticPlayerInterface {
  def newPlayer(name: String): PlayerInterface

  def checkNumberOfPlayers(number: Int): Boolean

  def playerPrediction(player: PlayerInterface, round: Int, trump: Option[String]): String

  def playerTurn(player: PlayerInterface, round: Int): String

  def fromXML(node: scala.xml.Node): PlayerInterface
}

