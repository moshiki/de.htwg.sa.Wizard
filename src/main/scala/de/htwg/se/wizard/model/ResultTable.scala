package de.htwg.se.wizard.model

case class ResultTable(roundsToPlay: Int = 20, numberOfPlayers: Int = 6) {
  val points: Array[Array[Int]] = Array.fill(roundsToPlay, numberOfPlayers)(0)

  def updatePoints(round: Int, player: Int, result: Int):Unit = {
    if (round == 1) points(round - 1)(player) = result
    else points(round - 1)(player) = result + points(round - 2)(player)
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
