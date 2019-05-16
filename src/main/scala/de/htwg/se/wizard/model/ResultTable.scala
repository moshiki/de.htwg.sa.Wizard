package de.htwg.se.wizard.model

case class ResultTable(rows: Int = 20, columns: Int = 6) {
  val points: Array[Array[Int]] = Array.fill(rows, columns)(0)

  def updatePoints(round: Int, player: Int, result: Int):Unit = points(round - 1)(player) = result
}
