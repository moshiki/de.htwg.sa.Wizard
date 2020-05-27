package de.htwg.sa.wizard.cardModule.util

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface

case class AssignCardsToPlayerArgumentContainer(cards: List[CardInterface], playerName: String)

object AssignCardsToPlayerArgumentContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[AssignCardsToPlayerArgumentContainer] = Json.writes[AssignCardsToPlayerArgumentContainer]
  implicit val containerReads: Reads[AssignCardsToPlayerArgumentContainer] = Json.reads[AssignCardsToPlayerArgumentContainer]
}


