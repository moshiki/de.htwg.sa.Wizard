package de.htwg.sa.wizard.cardModule.model.cardComponent

import play.api.libs.json.JsValue

trait CardStackInterface {
  val cards: List[CardInterface]
  def shuffleCards(): CardStackInterface
  def split(numberOfPlayers: Int, currentRound: Int): CardStackInterface
  def topOfCardStackString: String
  def playerOfHighestCard(cardList: List[CardInterface], color: Option[String]): String
  def fromJson(jsValue: JsValue): CardStackInterface
  def toJson: JsValue
}
