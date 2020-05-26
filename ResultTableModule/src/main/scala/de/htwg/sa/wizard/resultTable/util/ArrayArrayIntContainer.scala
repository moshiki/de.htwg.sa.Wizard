package de.htwg.sa.wizard.resultTable.util

case class ArrayArrayIntContainer(array: Array[Array[Int]])

object ArrayArrayIntContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[ArrayArrayIntContainer] = Json.writes[ArrayArrayIntContainer]
  implicit val containerReads: Reads[ArrayArrayIntContainer] = Json.reads[ArrayArrayIntContainer]
}
