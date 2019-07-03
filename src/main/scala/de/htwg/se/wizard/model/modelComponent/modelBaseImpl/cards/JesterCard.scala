package de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards


import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player

import scala.xml.Elem

case class JesterCard(owner: Option[Player] = None) extends Card(owner) {
  def hasColor: Boolean = false

  def isWizard: Boolean = false

  def isJester: Boolean = true

  override def getStringRep: String = "Jester"

  override def toXML: Elem = {
    <JesterCard>
      <owner>
        {if (owner.isDefined) owner.get.toXML
        else "None"}
      </owner>
    </JesterCard>
  }

  def fromXML(node: scala.xml.Node): JesterCard = {
    var owner: Option[Player] = None
    if ((node \ "owner" ).text.trim != "None") {
      val player = Player.fromXML((node \ "owner").head.child.filter(node => node.text.trim != "").head)
      owner = Some(player)
    }

    this.copy(owner = owner)
  }
}
