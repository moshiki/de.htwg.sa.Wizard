package de.htwg.sa.wizard.model.cardComponent.cardBaseImplementation

import de.htwg.sa.wizard.model.cardComponent.CardInterface
import play.api.libs.json.JsValue

import scala.xml.Elem


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
}


