package de.htwg.se.wizard.model.modelComponent.cards

import de.htwg.se.wizard.model.PlayerInterface

case class WizardCard(owner: Option[PlayerInterface] = None) extends Card(owner) {
  def hasColor: Boolean = false

  def isWizard: Boolean = true

  def isJester: Boolean = false

  override def getStringRep: String = "Wizard"

  override def toXML: String = {
    var ownerXML = "None"
    if (owner.isDefined) ownerXML = owner.get.toXML
    <WizardCard>
      <owner>
        {ownerXML}
      </owner>
    </WizardCard>
  }
}
