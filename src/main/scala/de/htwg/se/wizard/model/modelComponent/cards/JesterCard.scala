package de.htwg.se.wizard.model.modelComponent.cards

import de.htwg.se.wizard.model.PlayerInterface

import scala.xml.Elem

case class JesterCard(owner: Option[PlayerInterface] = None) extends Card(owner) {
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
}
