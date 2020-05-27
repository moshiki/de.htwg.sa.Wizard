package de.htwg.sa.wizard.cardModule.util

case class StringOptionContainer(option: Option[String])

object StringOptionContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[StringOptionContainer] = Json.writes[StringOptionContainer]
  implicit val containerReads: Reads[StringOptionContainer] = Json.reads[StringOptionContainer]
}
