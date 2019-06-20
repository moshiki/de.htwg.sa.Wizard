package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.SpecificPlayerInterface
import de.htwg.se.wizard.model.modelComponent.Player

abstract class Card(owner: Option[SpecificPlayerInterface]) {
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
}
object Card {
  def apply(card: String):Card = {
    card match {
      case "wizard" => WizardCard()
      case "jester" => JesterCard()
    }
  }

  def setOwner(card:Card, player: SpecificPlayerInterface):Card = {
    if(card.isJester) card.asInstanceOf[JesterCard].copy(owner = Some(player))
    else if(card.isWizard) card.asInstanceOf[WizardCard].copy(owner = Some(player))
    else card.asInstanceOf[DefaultCard].copy(owner = Some(player))
  }

  def getType(card: Card):Option[String] = {
    card match {
      case card: DefaultCard => Some(card.color)
      case _ => None
    }
  }

  def shuffleCards(cardStack: List[Card]): List[Card] = {
    CardStack.shuffleCards(cardStack)
  }

  def getPlayerOfHighestCard(cardList: List[Card], color: Option[String]): SpecificPlayerInterface = {
    CardStack.getPlayerOfHighestCard(cardList, color)
  }

  def initializeCardStack(): List[Card] = {
    CardStack.initialize
  }
}