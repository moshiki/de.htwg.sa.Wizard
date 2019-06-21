package de.htwg.se.wizard.model

trait ResultTableInterface {
  override def toString: String

  def updatePoints(round: Int, player: Int, result: Int): ResultTableInterface

  def toAnyArray: Array[Array[Any]]
}

trait ResultTableBuilderInterface {
  def initializeTable(roundsToPlay: Int = 20, numberOfPlayers: Int = 6): ResultTableInterface
}