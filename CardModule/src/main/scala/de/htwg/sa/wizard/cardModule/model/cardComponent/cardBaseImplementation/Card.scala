package de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface
import play.api.libs.json.JsValue

import scala.xml.{Elem, Node}


abstract class Card(owner: Option[String]) extends CardInterface {
  def hasColor: Boolean

  def isWizard: Boolean

  def isJester: Boolean

  def hasOwner: Boolean = owner.isDefined

  def ownerName: String = owner match {
    case Some(owner) => owner
    case _ => "unknown"
  }

  def toString: String

  def toXML: Elem

  def ownerString: String = owner match {
    case Some(owner) => owner
    case None => "None"
  }

  def toJson: JsValue

  def fromJson(jsValue: JsValue): CardInterface

  def setOwner(player: String): CardInterface

  def fromXML(node: Node): CardInterface = {
    node.label match {
      case "WizardCard" => WizardCard().wizardFromXML(node)
      case "JesterCard" => JesterCard().jesterFromXML(node)
      case "DefaultCard" => DefaultCard("blue", 1).defaultFromXML(node)
    }
  }
}


