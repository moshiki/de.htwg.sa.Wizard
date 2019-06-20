package de.htwg.se.wizard.model.modelComponent.cards

import de.htwg.se.wizard.model.{CardInterface, SpecificCardInterface, SpecificPlayerInterface}


abstract class Card(owner: Option[SpecificPlayerInterface]) extends SpecificCardInterface {
  override def hasColor: Boolean

  override def isWizard: Boolean

  override def isJester: Boolean

  def hasOwner: Boolean = owner.isDefined

  def ownerName: String = owner match {
    case Some(player) => player.getName
    case _ => "unknown"
  }

  def getStringRep: String

  override def toString: String = "cards/" + getStringRep
}



object Card extends CardInterface  {
  def apply(card: String):Card = {
    card match {
      case "wizard" => WizardCard()
      case "jester" => JesterCard()
    }
  }

  override def shuffleCards(cardStack: List[SpecificCardInterface]): List[SpecificCardInterface] = {
    CardStack.shuffleCards(cardStack)
  }

  override def initializeCardStack(): List[SpecificCardInterface] = {
    CardStack.initialize
  }

  override def getPlayerOfHighestCard(cardList: List[SpecificCardInterface], color: Option[String]): SpecificPlayerInterface = {
    CardStack.getPlayerOfHighestCard(cardList, color)
  }

  override def getType(card: SpecificCardInterface):Option[String] = {
    card match {
      case card: DefaultCard => Some(card.color)
      case _ => None
    }
  }

  override def setOwner(card:SpecificCardInterface, player: SpecificPlayerInterface):Card = {
    if(card.isJester) card.asInstanceOf[JesterCard].copy(owner = Some(player))
    else if(card.isWizard) card.asInstanceOf[WizardCard].copy(owner = Some(player))
    else card.asInstanceOf[DefaultCard].copy(owner = Some(player))
  }
}