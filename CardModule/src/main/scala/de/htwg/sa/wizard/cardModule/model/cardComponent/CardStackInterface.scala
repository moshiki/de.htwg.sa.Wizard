package de.htwg.sa.wizard.cardModule.model.cardComponent

trait CardStackInterface {
  val cards: List[CardInterface]
  def shuffleCards(): CardStackInterface
  def split(numberOfPlayers: Int, currentRound: Int): CardStackInterface
  def topOfCardStackString: String
  def playerOfHighestCard(cardList: List[CardInterface], color: Option[String]): String
}