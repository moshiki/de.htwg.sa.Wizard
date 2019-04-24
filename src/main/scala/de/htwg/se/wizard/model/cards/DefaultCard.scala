package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player

case class DefaultCard(color: String, number: Integer, owner: Player) extends Card with Ordered[DefaultCard] {
  require(number >= 1 && number <= 13)
  require(color == "blue" || color == "red" || color == "yellow" || color == "green")

  def hasColor: Boolean = true

  def isWizard: Boolean = false

  def isJester: Boolean = false

  def hasOwner: Boolean = owner != null

  override def toString: String = color + " " + number

  override def compare(that: DefaultCard): Int = this.number - that.number

  override def equals(obj: Any): Boolean = {
    obj match {
      case defaultCard: DefaultCard => compare(defaultCard) == 0
      case _ => false
    }
  }
}
