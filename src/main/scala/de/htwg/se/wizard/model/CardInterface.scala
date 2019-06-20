package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.modelComponent.cards.Card

trait CardInterface {
  def initializeCardStack(): List[SpecificCardInterface]

  def shuffleCards(cardStack: List[SpecificCardInterface]): List[SpecificCardInterface]

  def apply(card: String): SpecificCardInterface

  def getPlayerOfHighestCard(cardList: List[SpecificCardInterface], color: Option[String]): SpecificPlayerInterface

  def getType(card: SpecificCardInterface): Option[String]

  def setOwner(card: SpecificCardInterface, player: SpecificPlayerInterface): SpecificCardInterface
}

trait SpecificCardInterface {
  def hasColor: Boolean
  def isJester: Boolean
  def isWizard: Boolean
}