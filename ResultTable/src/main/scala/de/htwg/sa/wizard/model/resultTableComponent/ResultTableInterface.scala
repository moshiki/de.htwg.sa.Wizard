package de.htwg.sa.wizard.model.resultTableComponent

import scala.xml.{Elem, Node}

trait ResultTableInterface {
  def updatePoints(round: Int, player: Int, result: Int): ResultTableInterface

  def toAnyArray: Array[Array[Any]]

  def toString: String

  def toXML: Elem

  def fromXML(node: Node): ResultTableInterface
}
