package de.htwg.se.wizard.model.modelComponent.cards

import de.htwg.se.wizard.model.{CardInterface, SpecificPlayerInterface}


abstract class Card(owner: Option[SpecificPlayerInterface]) extends CardInterface {
  def hasColor: Boolean

  def isWizard: Boolean

  def isJester: Boolean

  def hasOwner: Boolean = owner.isDefined

  def ownerName: String = owner match {
    case Some(player) => player.getName
    case _ => "unknown"
  }

  def getStringRep: String

  override def toString: String = "cards/" + getStringRep

  override def getPlayerOfHighestCard(cardList: List[Card], color: Option[String]): SpecificPlayerInterface = {
    CardStack.getPlayerOfHighestCard(cardList, color)
  }

  override def getType(card: Card):Option[String] = {
    card match {
      case card: DefaultCard => Some(card.color)
      case _ => None
    }
  }

  override def setOwner(card:Card, player: SpecificPlayerInterface):Card = {
    if(card.isJester) card.asInstanceOf[JesterCard].copy(owner = Some(player))
    else if(card.isWizard) card.asInstanceOf[WizardCard].copy(owner = Some(player))
    else card.asInstanceOf[DefaultCard].copy(owner = Some(player))
  }

}



object Card extends CardInterface  {
  def apply(card: String):Card = {
    card match {
      case "wizard" => WizardCard()
      case "jester" => JesterCard()
    }
  }

  override def shuffleCards(cardStack: List[CardInterface]): List[CardInterface] = {
    CardStack.shuffleCards(cardStack)
  }

  override def initializeCardStack(): List[Card] = {
    CardStack.initialize
  }

  override def getPlayerOfHighestCard(cardList: List[Card], color: Option[String]): SpecificPlayerInterface = ???

  override def getType(card: Card): Option[String] = ???

  override def setOwner(card: Card, player: SpecificPlayerInterface): Card = ???
}