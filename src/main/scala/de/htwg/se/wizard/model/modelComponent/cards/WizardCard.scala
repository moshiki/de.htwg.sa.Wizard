package de.htwg.se.wizard.model.modelComponent.cards

import de.htwg.se.wizard.model.SpecificPlayerInterface
import de.htwg.se.wizard.model.modelComponent.Player

case class WizardCard(owner: Option[SpecificPlayerInterface] = None) extends Card(owner) {
  def hasColor: Boolean = false

  def isWizard: Boolean = true

  def isJester: Boolean = false

  override def getStringRep: String = "Wizard"
}
