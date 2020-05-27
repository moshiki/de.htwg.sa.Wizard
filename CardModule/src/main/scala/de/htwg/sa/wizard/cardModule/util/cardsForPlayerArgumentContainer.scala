package de.htwg.sa.wizard.cardModule.util

case class cardsForPlayerArgumentContainer(playerNumber: Int, currentRound: Int)

object cardsForPlayerArgumentContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[cardsForPlayerArgumentContainer] = Json.writes[cardsForPlayerArgumentContainer]
  implicit val containerReads: Reads[cardsForPlayerArgumentContainer] = Json.reads[cardsForPlayerArgumentContainer]
}


