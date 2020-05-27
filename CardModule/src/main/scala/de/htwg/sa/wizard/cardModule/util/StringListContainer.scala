package de.htwg.sa.wizard.cardModule.util

case class StringListContainer(list: List[String])

object StringListContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[StringListContainer] = Json.writes[StringListContainer]
  implicit val containerReads: Reads[StringListContainer] = Json.reads[StringListContainer]
}
