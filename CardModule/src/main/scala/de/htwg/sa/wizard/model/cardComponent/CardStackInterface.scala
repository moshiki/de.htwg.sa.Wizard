package de.htwg.sa.wizard.model.cardComponent

import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player

trait CardStackInterface {
  val initialize: List[CardInterface]
  def shuffleCards(a: List[CardInterface]): List[CardInterface]

  //ToDo: Abhängigkeit zu PlayerObjekt auflösen
  def playerOfHighestCard(cardList: List[CardInterface], color: Option[String]): Option[Player]
}