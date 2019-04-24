package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player

case class WizardCard(owner: Option[Player] = None) extends Card(owner) {
  def hasColor: Boolean = false

  def isWizard: Boolean = true

  def isJester: Boolean = false

  override def toString: String = "Wizard"
}
