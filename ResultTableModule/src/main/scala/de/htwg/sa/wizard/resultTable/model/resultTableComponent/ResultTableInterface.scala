package de.htwg.sa.wizard.resultTable.model.resultTableComponent

import play.api.libs.json.JsValue

import scala.xml.{Elem, Node}

trait ResultTableInterface {
  def updatePoints(round: Int, player: Int, result: Int): ResultTableInterface

  def toArray: Array[Array[Int]]

  def toString: String

  def toXML: Elem

  def fromXML(node: Node): ResultTableInterface

  def toJson: JsValue

  def fromJson(jsValue: JsValue): ResultTableInterface

  def initializeTable(roundsToPlay: Int, numberOfPlayers: Int): ResultTableInterface

  def storePlayerNames(playerNames: List[String]): ResultTableInterface

  def playerNameList: List[String]

  def recreateWithData(roundsToPlay: Int, numberOfPlayers: Int, points: Vector[Vector[Int]], playerNames: List[String]): ResultTableInterface
}
