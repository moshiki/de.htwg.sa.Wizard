package de.htwg.sa.wizard.model.cardComponent

import de.htwg.sa.wizard.model.cardComponent.cardBaseImplementation.{Card, DefaultCard, JesterCard, WizardCard}
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player
import play.api.libs.json.JsValue

import scala.xml.{Elem, Node}

trait CardInterface {
  val owner: Option[Player]
  def hasColor: Boolean
  def isWizard: Boolean
  def isJester: Boolean
  def hasOwner: Boolean
  def ownerName: String
  def toString: String
  def toXML: Elem
  def ownerString: String
  def toJson: JsValue
  def fromJson(jsValue: JsValue): CardInterface
}

object CardInterface {
  def apply(card: String): CardInterface = {
    card match {
      case "wizard" => WizardCard()
      case "jester" => JesterCard()
    }
  }

  def setOwner(card:CardInterface, player: Player): CardInterface = {
    if(card.isJester) card.asInstanceOf[JesterCard].copy(owner = Some(player))
    else if(card.isWizard) card.asInstanceOf[WizardCard].copy(owner = Some(player))
    else card.asInstanceOf[DefaultCard].copy(owner = Some(player))
  }

  def fromXML(node: Node): CardInterface = {
    node.label match {
      case "WizardCard" => WizardCard().fromXML(node)
      case "JesterCard" => JesterCard().fromXML(node)
      case "DefaultCard" => DefaultCard("blue", 1).fromXML(node)
    }
  }

  import play.api.libs.json._
  implicit val cardWrites: Writes[CardInterface] = {
    case jesterCard: JesterCard => jesterCard.toJson
    case defaultCard: DefaultCard => defaultCard.toJson
    case wizardCard: WizardCard => wizardCard.toJson
  }

  implicit val cardReads: Reads[CardInterface] = (json: JsValue) => {
    val cardType = (json \ "type").get.as[String]
    val card = cardType match {
      case "Wizard" => WizardCard().fromJson(json)
      case "Jester" => JesterCard().fromJson(json)
      case "Default" => DefaultCard("blue", 1).fromJson(json)
    }
    JsSuccess(card)
  }
}

