package de.htwg.sa.wizard.cardModule.model.cardComponent

trait CardStackInterface {
  val initialize: List[CardInterface]
  def shuffleCards(a: List[CardInterface]): List[CardInterface]

  def playerOfHighestCard(cardList: List[CardInterface], color: Option[String]): Option[String]
}
