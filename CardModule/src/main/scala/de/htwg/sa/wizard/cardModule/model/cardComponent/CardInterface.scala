package de.htwg.sa.wizard.cardModule.model.cardComponent

import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.{DefaultCard, JesterCard, WizardCard}
import play.api.libs.json.JsValue

import scala.xml.{Elem, Node}


trait CardInterface {
  val owner: Option[String]
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
  def fromXML(node: Node): CardInterface
  def setOwner(player: String): CardInterface
}

object CardInterface {
  def apply(card: String): CardInterface = {
    card match {
      case "DefaultCard" => DefaultCard("blue", 1)
      case "WizardCard" => WizardCard()
      case "JesterCard" => JesterCard()
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

