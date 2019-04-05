package de.htwg.se.wizard.model

class WizardClass(owner: Player) extends Card {
  def hasColor: Boolean = false
  def isWizard: Boolean = true
  def isJester: Boolean = false
  def hasOwner: Boolean = owner != null
}
