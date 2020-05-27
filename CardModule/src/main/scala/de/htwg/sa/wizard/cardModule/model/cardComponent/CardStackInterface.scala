package de.htwg.sa.wizard.cardModule.model.cardComponent

trait CardStackInterface {
  val cards: List[CardInterface]
  def shuffleCards(): CardStackInterface
  //def playerOfHighestCard(cardList: List[CardInterface], color: Option[String]): Option[String]
}