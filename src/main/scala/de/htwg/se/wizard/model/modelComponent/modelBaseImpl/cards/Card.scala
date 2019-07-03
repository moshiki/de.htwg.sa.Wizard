package de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards

import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player
import play.api.libs.json.JsValue

import scala.xml.{Elem, Node}


abstract class Card(owner: Option[Player]) {
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

  def toXML: Elem

  def toJson: JsValue
}



object Card {
  def apply(card: String):Card = {
    card match {
      case "wizard" => WizardCard()
      case "jester" => JesterCard()
    }
  }

  def setOwner(card:Card, player: Player): Card = {
    if(card.isJester) card.asInstanceOf[JesterCard].copy(owner = Some(player))
    else if(card.isWizard) card.asInstanceOf[WizardCard].copy(owner = Some(player))
    else card.asInstanceOf[DefaultCard].copy(owner = Some(player))
  }

  def fromXML(node: Node): Card = {
    node.label match {
      case "WizardCard" => WizardCard().fromXML(node)
      case "JesterCard" => JesterCard().fromXML(node)
      case "DefaultCard" => DefaultCard("blue", 1).fromXML(node)
    }
  }

  import play.api.libs.json._
  implicit val cardWrites: Writes[Card] = {
    case jesterCard: JesterCard => jesterCard.toJson
    case defaultCard: DefaultCard => defaultCard.toJson
    case wizardCard: WizardCard => wizardCard.toJson
  }

  //implicit val cardReads = ???
}