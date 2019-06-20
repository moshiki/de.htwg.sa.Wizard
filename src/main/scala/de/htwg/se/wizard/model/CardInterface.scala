package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.modelComponent.cards.Card

trait CardInterface {
  def initializeCardStack(): List[CardInterface]

  def shuffleCards(cardStack: List[CardInterface]): List[CardInterface]

  def apply(card: String):Card

  def getPlayerOfHighestCard(cardList: List[Card], color: Option[String]): SpecificPlayerInterface

  def getType(card: Card):Option[String]

  def setOwner(card:Card, player: SpecificPlayerInterface):Card

}