package de.htwg.se.wizard.model

import scala.xml.Elem

trait ResultTableInterface {
  override def toString: String

  def updatePoints(round: Int, player: Int, result: Int): ResultTableInterface

  def toAnyArray: Array[Array[Any]]

  def toXML: Elem
}

trait ResultTableBuilderInterface {
  def initializeTable(roundsToPlay: Int = 20, numberOfPlayers: Int = 6): ResultTableInterface

  def fromXML(node: scala.xml.Node): ResultTableInterface
}