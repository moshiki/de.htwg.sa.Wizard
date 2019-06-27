package de.htwg.se.wizard.model.modelComponent.cards

import de.htwg.se.wizard.model.PlayerInterface
import de.htwg.se.wizard.model.modelComponent.StaticPlayer

import scala.xml.Elem

case class DefaultCard(color: String, number: Int, owner: Option[PlayerInterface] = None)
  extends Card(owner) with Ordered[DefaultCard] {
  require(number >= 1 && number <= 13)
  require(color == "blue" || color == "red" || color == "yellow" || color == "green")

  def hasColor: Boolean = true

  def isWizard: Boolean = false

  def isJester: Boolean = false

  override def getStringRep: String = color + " " + number

  override def compare(that: DefaultCard): Int = this.number - that.number

  override def equals(obj: Any): Boolean = {
    obj match {
      case defaultCard: DefaultCard => compare(defaultCard) == 0
      case _ => false
    }
  }

  override def toXML: Elem = {
    <DefaultCard>
      <color>{color}</color>
      <number>{number}</number>
      <owner>{if (owner.isDefined) owner.get.toXML
      else "None"}</owner>
    </DefaultCard>
  }

  def fromXML(node: scala.xml.Node): DefaultCard = {
    val color = (node \ "color").text.trim

    val number = (node \ "color").text.trim.toInt

    var owner: Option[PlayerInterface] = None
    if ((node \ "owner" ).text.trim != "None") {
      val player = StaticPlayer().fromXML((node \ "owner").head)
      owner = Some(player)
    }

    this.copy(color = color, number = number, owner = owner)
  }
}
