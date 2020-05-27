package de.htwg.sa.wizard.cardModule.util

case class CardsForPlayerArgumentContainer(playerNumber: Int, currentRound: Int)

object CardsForPlayerArgumentContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[CardsForPlayerArgumentContainer] = Json.writes[CardsForPlayerArgumentContainer]
  implicit val containerReads: Reads[CardsForPlayerArgumentContainer] = Json.reads[CardsForPlayerArgumentContainer]
}


