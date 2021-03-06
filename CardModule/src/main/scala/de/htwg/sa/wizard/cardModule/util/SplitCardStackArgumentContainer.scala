package de.htwg.sa.wizard.cardModule.util

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

case class SplitCardStackArgumentContainer(numberOfPlayers: Int, currentRound: Int)

object SplitCardStackArgumentContainer extends PlayJsonSupport {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[SplitCardStackArgumentContainer] = Json.writes[SplitCardStackArgumentContainer]
  implicit val containerReads: Reads[SplitCardStackArgumentContainer] = Json.reads[SplitCardStackArgumentContainer]
}


