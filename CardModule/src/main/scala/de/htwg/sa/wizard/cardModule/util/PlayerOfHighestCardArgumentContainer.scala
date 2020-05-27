package de.htwg.sa.wizard.cardModule.util

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface

case class PlayerOfHighestCardArgumentContainer(list: List[CardInterface])

object PlayerOfHighestCardArgumentContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[PlayerOfHighestCardArgumentContainer] = Json.writes[PlayerOfHighestCardArgumentContainer]
  implicit val containerReads: Reads[PlayerOfHighestCardArgumentContainer] = Json.reads[PlayerOfHighestCardArgumentContainer]
}
