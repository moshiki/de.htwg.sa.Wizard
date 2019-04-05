package de.htwg.se.wizard.model.cards

abstract class Card() {
  def hasColor: Boolean
  def isWizard: Boolean
  def isJester: Boolean
  def hasOwner: Boolean
}