package de.htwg.se.wizard.model.modelComponent.cards

import de.htwg.se.wizard.model.{CardInterface, PlayerInterface, StaticCardInterface}


abstract class Card(owner: Option[PlayerInterface]) extends CardInterface {
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



case class StaticCard() extends StaticCardInterface  {
  def apply(card: String):Card = {
    card match {
      case "wizard" => WizardCard()
      case "jester" => JesterCard()
    }
  }

  override def shuffleCards(cardStack: List[CardInterface]): List[CardInterface] = {
    CardStack.shuffleCards(cardStack)
  }

  override def initializeCardStack(): List[CardInterface] = {
    CardStack.initialize
  }

  override def getPlayerOfHighestCard(cardList: List[CardInterface], color: Option[String]): PlayerInterface = {
    CardStack.getPlayerOfHighestCard(cardList, color)
  }

  override def getType(card: CardInterface):Option[String] = {
    card match {
      case card: DefaultCard => Some(card.color)
      case _ => None
    }
  }

  override def setOwner(card:CardInterface, player: PlayerInterface):Card = {
    if(card.isJester) card.asInstanceOf[JesterCard].copy(owner = Some(player))
    else if(card.isWizard) card.asInstanceOf[WizardCard].copy(owner = Some(player))
    else card.asInstanceOf[DefaultCard].copy(owner = Some(player))
  }
}