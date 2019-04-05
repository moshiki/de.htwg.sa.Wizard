package de.htwg.se.wizard.model

case class Card(color: String, number: Integer, owner: Player) {
  def hasColor: Boolean = color != null || color == "none"
  def isWizard: Boolean = number > 13
  def isJester: Boolean = number < 1
  def hasOwner: Boolean = owner != null
}