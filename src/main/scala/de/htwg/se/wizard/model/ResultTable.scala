package de.htwg.se.wizard.model

case class ResultTable(roundsToPlay: Int = 20, numberOfPlayers: Int = 6, points: Vector[Vector[Int]]) {

  def updatePoints(round: Int, player: Int, result: Int): ResultTable = {
    if (round == 1) this.copy(points = points.updated(round - 1, points(round - 1).updated(player, result)))
    else this.copy(points = points.updated(round - 1, points(round - 1).updated(player, result + points(round - 2)(player))))
  }

  def toAnyArray: Array[Array[Any]] = {
    points.toArray map(innerVector => innerVector.toArray[Any])
  }

  override def toString: String = {
    val horizontalBar = "#" + ("##############" * numberOfPlayers)

    def oneLine(line: Int) = points(line).mkString("#      ", "      #      ", "      #")

    var returnString = "#"
    for (i <- 1 to numberOfPlayers) {
      returnString += "  Player  " + i + "  #"
    }

    returnString += "\n"

    for (i <- 0 until roundsToPlay) {
      returnString += horizontalBar + "\n"
      returnString += oneLine(i) + "\n"
    }

    returnString += horizontalBar
    returnString
  }
}

object ResultTable {
  def initializeVector(roundsToPlay: Int = 20, numberOfPlayers: Int = 6): Vector[Vector[Int]] = {
    Vector.fill(roundsToPlay, numberOfPlayers)(0)
  }
}
