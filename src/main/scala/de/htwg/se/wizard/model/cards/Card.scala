package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player

abstract class Card(owner: Option[Player] = None) {
  def hasColor: Boolean

  def isWizard: Boolean

  def isJester: Boolean

  def hasOwner: Boolean = owner.isDefined

  def ownerName: String = owner match {
    case Some(player) => player.name
    case _ => "unknown"
  }

  override def toString: String
}