package de.htwg.se.wizard.model.modelComponent.cards

import de.htwg.se.wizard.model.PlayerInterface
import de.htwg.se.wizard.model.modelComponent.{Player, StaticPlayer}

import scala.xml.Elem

case class WizardCard(owner: Option[PlayerInterface] = None) extends Card(owner) {
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
    var owner: Option[PlayerInterface] = None
    if ((node \ "owner" ).text.trim != "None") {
      val player = StaticPlayer().fromXML((node \ "owner").head)
      owner = Some(player)
    }

    this.copy(owner = owner)
  }
}
