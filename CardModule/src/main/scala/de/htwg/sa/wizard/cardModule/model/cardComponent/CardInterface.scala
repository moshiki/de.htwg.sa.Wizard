package de.htwg.sa.wizard.cardModule.model.cardComponent

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.{DefaultCard, JesterCard, WizardCard}
import play.api.libs.json._

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
  def typeString: String
  def value: Option[Int]
  def colorOption: Option[String]
}

object CardInterface extends PlayJsonSupport {
  def apply(card: String): CardInterface = {
    card match {
      case "DefaultCard" => DefaultCard("blue", 1)
      case "WizardCard" => WizardCard()
      case "JesterCard" => JesterCard()
    }
  }

  def buildCard(cardType: String, color: Option[String], owner: Option[String], value: Option[Int]): CardInterface = {
    cardType match {
      case "DefaultCard" => DefaultCard(color.get, value.get, owner)
      case "WizardCard" => WizardCard(owner)
      case "JesterCard" => JesterCard(owner)
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
    val cardOwner = (json \ "owner").asOpt[String]
    val card = cardType match {
      case "Wizard" => WizardCard(cardOwner).fromJson(json)
      case "Jester" => JesterCard(cardOwner).fromJson(json)
      case "Default" => DefaultCard("blue", 1, cardOwner).fromJson(json)
    }
    JsSuccess(card)
  }
}

