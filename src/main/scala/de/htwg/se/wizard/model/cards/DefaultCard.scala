package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player

case class DefaultCard(color: String, number: Integer, owner: Player) extends Card {
  require(number >= 1 && number <= 13)
  require(color == "blue" || color == "red" || color == "yellow" || color == "green")
  def hasColor: Boolean = true
  def isWizard: Boolean = false
  def isJester: Boolean = false
  def hasOwner: Boolean = owner != null
}
