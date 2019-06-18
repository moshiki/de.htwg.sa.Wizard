package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player

abstract class Card(owner: Option[Player]) {
  def hasColor: Boolean

  def isWizard: Boolean

  def isJester: Boolean

  def hasOwner: Boolean = owner.isDefined

  def ownerName: String = owner match {
    case Some(player) => player.name
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

  def setOwner(card:Card, player: Player):Card = {
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
}