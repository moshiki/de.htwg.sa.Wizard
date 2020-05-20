package de.htwg.sa.wizard.controller.controllerComponent

trait ResultTableControllerInterface {
  def updatePoints(round: Int, points: Vector[Int]): Unit

  def initializeTable(numberOfRounds: Int, numberOfPlayers: Int): Unit

  def safe(): Unit

  def load(): Unit

  def pointArrayForView: Array[Array[Any]]

  def tableAsString: String

  def playerList: List[String]
}
