package de.htwg.se.wizard.model

trait StaticCardInterface {
  def initializeCardStack(): List[CardInterface]

  def shuffleCards(cardStack: List[CardInterface]): List[CardInterface]

  def apply(card: String): CardInterface

  def getPlayerOfHighestCard(cardList: List[CardInterface], color: Option[String]): PlayerInterface

  def getType(card: CardInterface): Option[String]

  def setOwner(card: CardInterface, player: PlayerInterface): CardInterface
}

trait CardInterface {
  def hasColor: Boolean
  def isJester: Boolean
  def isWizard: Boolean
  def toXML: String
}