package de.htwg.sa.wizard.resultTable.util

case class InitializeTableArgumentContainer(numberOfRounds: Int, numberOfPlayers: Int)

object InitializeTableArgumentContainer {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[InitializeTableArgumentContainer] = Json.writes[InitializeTableArgumentContainer]
  implicit val containerReads: Reads[InitializeTableArgumentContainer] = Json.reads[InitializeTableArgumentContainer]
}
