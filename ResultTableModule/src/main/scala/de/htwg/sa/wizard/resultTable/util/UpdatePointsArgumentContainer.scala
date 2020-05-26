package de.htwg.sa.wizard.resultTable.util

case class UpdatePointsArgumentContainer(round: Int, points: Vector[Int])

object UpdatePointsArgumentContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[UpdatePointsArgumentContainer] = Json.writes[UpdatePointsArgumentContainer]
  implicit val containerReads: Reads[UpdatePointsArgumentContainer] = Json.reads[UpdatePointsArgumentContainer]
}
