package de.htwg.sa.wizard.model.cardComponent

trait CardStackInterface {
  val initialize: List[CardInterface]
  def shuffleCards(a: List[CardInterface]): List[CardInterface]

  //ToDo: Abhängigkeit zu PlayerObjekt auflösen: Playername als String
  def playerOfHighestCard(cardList: List[CardInterface], color: Option[String]): Option[String]
}