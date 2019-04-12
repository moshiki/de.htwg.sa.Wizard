package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player

case class WizardCard(owner: Player) extends Card {
  def hasColor: Boolean = false
  def isWizard: Boolean = true
  def isJester: Boolean = false
  def hasOwner: Boolean = owner != null

  override def toString: String = "Wizard"
}
