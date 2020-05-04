package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import de.vandermeer.asciitable.AsciiTable

import scala.xml.{Elem, Node}

case class ResultTable(roundsToPlay: Int = 20, numberOfPlayers: Int = 6, points: Vector[Vector[Int]]) {

  def updatePoints(round: Int, player: Int, result: Int): ResultTable = {
    if (round == 1) this.copy(points = points.updated(round - 1, points(round - 1).updated(player, result)))
    else this.copy(points = points.updated(round - 1, points(round - 1).updated(player, result + points(round - 2)(player))))
  }

  def toAnyArray: Array[Array[Any]] = {
    points.toArray map(innerVector => innerVector.toArray[Any])
  }

  override def toString: String = {
    import scala.jdk.CollectionConverters._
    val tableHeader = 1 to numberOfPlayers map("Player " + _)
    val table = new AsciiTable()
    table.addRule()
    table addRow tableHeader.asJava
    table.addRule()
    points foreach { pointsPerRound => table addRow pointsPerRound.asJava; table.addRule()}
    table.render()
  }

  def toXML: Elem = {
    <ResultTable>
      <roundsToPlay>{roundsToPlay}</roundsToPlay>
      <numberOfPlayers>{numberOfPlayers}</numberOfPlayers>
      <points>{points.flatMap(vector => vector.map(point => <point>{point}</point>))}</points>
    </ResultTable>
  }

  def fromXML(node: Node): ResultTable = {
    val roundsToPlay = (node \ "roundsToPlay").text.trim.toInt
    val numberOfPlayers = (node \ "numberOfPlayers").text.trim.toInt
    val points = (node \ "points").head.child
    val pointList = points.map(node => (node \\ "point").text.toInt)
    val newTable = ResultTable.initializeTable(roundsToPlay, numberOfPlayers)
    def buildVector (splitAt: Int, seq: Seq[Int], vector: Vector[Vector[Int]]): Vector[Vector[Int]] = vector ++ {
        seq.length match {
          case len if len > splitAt =>
            val split = seq.splitAt(splitAt)
            buildVector(splitAt, split._2, Vector(split._1.toVector))
          case _ => Vector(seq.toVector)
        }
      }
    newTable.copy(points = buildVector(numberOfPlayers, pointList.toList, Vector()))
  }
}

object ResultTable {
  def initializeTable(roundsToPlay: Int = 20, numberOfPlayers: Int = 6): ResultTable = {
    val vector = Vector.fill(roundsToPlay, numberOfPlayers)(0)
    ResultTable(roundsToPlay, numberOfPlayers, vector)
  }

  import play.api.libs.json._
  implicit val resultTableWrites: OWrites[ResultTable] = Json.writes[ResultTable]
  implicit val resultTableReads: Reads[ResultTable] = Json.reads[ResultTable]
}
