package de.htwg.sa.wizard.cardModule.util

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface

case class StringListContainer(list: List[CardInterface])

object StringListContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[StringListContainer] = Json.writes[StringListContainer]
  implicit val containerReads: Reads[StringListContainer] = Json.reads[StringListContainer]
}
