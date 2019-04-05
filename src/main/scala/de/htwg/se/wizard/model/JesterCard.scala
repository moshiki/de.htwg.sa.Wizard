package de.htwg.se.wizard.model

case class JesterCard(owner: Player) extends Card {
  def hasColor: Boolean = false
  def isWizard: Boolean = false
  def isJester: Boolean = true
  def hasOwner: Boolean = owner != null
}
