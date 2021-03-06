package de.htwg.sa.wizard.resultTable.controller.controllerComponent

trait ResultTableControllerInterface {
  def updatePoints(round: Int, points: Vector[Int]): Unit

  def initializeTable(numberOfRounds: Int, numberOfPlayers: Int): Unit

  def save(): Unit

  def load(): Unit

  def pointArrayForView: Array[Array[Int]]

  def tableAsString: String

  def playerList: List[String]
}
