package de.htwg.se.wizard.model.modelComponent

import de.htwg.se.wizard.model.{ResultTableBuilderInterface, ResultTableInterface}

import scala.xml.{Elem, Node}

case class ResultTable(roundsToPlay: Int = 20, numberOfPlayers: Int = 6, points: Vector[Vector[Int]]) extends ResultTableInterface {

  override def updatePoints(round: Int, player: Int, result: Int): ResultTable = {
    if (round == 1) this.copy(points = points.updated(round - 1, points(round - 1).updated(player, result)))
    else this.copy(points = points.updated(round - 1, points(round - 1).updated(player, result + points(round - 2)(player))))
  }

  override def toAnyArray: Array[Array[Any]] = {
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

  override def toXML: Elem = { //TODO: Test this
    <ResultTable>
      <roundsToPlay>{roundsToPlay}</roundsToPlay>
      <numberOfPlayers>{numberOfPlayers}</numberOfPlayers>
      <points>{points.map(vector => for (i <- vector.indices) yield <point>{vector(i)}</point>)}</points>
    </ResultTable>
  }
}

case class ResultTableBuilder() extends ResultTableBuilderInterface {
  override def initializeTable(roundsToPlay: Int = 20, numberOfPlayers: Int = 6): ResultTable = {
    val vector = Vector.fill(roundsToPlay, numberOfPlayers)(0)
    ResultTable(roundsToPlay, numberOfPlayers, vector)
  }

  override def fromXML(node: Node): ResultTableInterface = ???
}
