package de.htwg.sa.wizard.cardModule.util
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

case class CardsForPlayerArgumentContainer(playerNumber: Int, currentRound: Int)

object CardsForPlayerArgumentContainer extends PlayJsonSupport {
  import play.api.libs.json._

  implicit val containerWrites: OWrites[CardsForPlayerArgumentContainer] = Json.writes[CardsForPlayerArgumentContainer]

  implicit val containerReads: Reads[CardsForPlayerArgumentContainer] = Json.reads[CardsForPlayerArgumentContainer]
}


