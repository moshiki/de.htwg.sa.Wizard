package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player

case class JesterCard(owner: Option[Player] = None) extends Card(owner) {
  def hasColor: Boolean = false

  def isWizard: Boolean = false

  def isJester: Boolean = true

  override def getStringRep: String = "Jester"
}
