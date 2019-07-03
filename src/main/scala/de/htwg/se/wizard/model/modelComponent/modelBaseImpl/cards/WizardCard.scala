package de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards

import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player

import scala.xml.Elem

case class WizardCard(owner: Option[Player] = None) extends Card(owner) {
  def hasColor: Boolean = false

  def isWizard: Boolean = true

  def isJester: Boolean = false

  override def getStringRep: String = "Wizard"

  override def toXML: Elem = {
    <WizardCard>
      <owner>
        {if (owner.isDefined) owner.get.toXML
      else "None"}
      </owner>
    </WizardCard>
  }

  def fromXML(node: scala.xml.Node): WizardCard = {
    var owner: Option[Player] = None
    if ((node \ "owner" ).text.trim != "None") {
      val player = Player.fromXML((node \ "owner").head.child.filter(node => node.text.trim != "").head)
      owner = Some(player)
    }

    this.copy(owner = owner)
  }
}
