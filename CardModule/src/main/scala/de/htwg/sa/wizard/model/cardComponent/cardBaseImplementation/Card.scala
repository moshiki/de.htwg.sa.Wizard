package de.htwg.sa.wizard.model.cardComponent.cardBaseImplementation

import de.htwg.sa.wizard.model.cardComponent.CardInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player
import play.api.libs.json.JsValue

import scala.xml.{Elem, Node}


abstract class Card(owner: Option[Player]) extends CardInterface {
  def hasColor: Boolean

  def isWizard: Boolean

  def isJester: Boolean

  def hasOwner: Boolean = owner.isDefined

  def ownerName: String = owner match {
    case Some(player) => player.name
    case _ => "unknown"
  }

  def toString: String

  def toXML: Elem

  def ownerString: String = owner match {
    case Some(player) => player.toString
    case None => "None"
  }

  def toJson: JsValue

  def fromJson(jsValue: JsValue): CardInterface
}


