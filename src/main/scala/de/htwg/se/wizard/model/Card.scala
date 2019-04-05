package de.htwg.se.wizard.model

abstract class Card() {
  def hasColor: Boolean
  def isWizard: Boolean
  def isJester: Boolean
  def hasOwner: Boolean
}