package de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards


import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player
import play.api.libs.json.{JsValue, Json}

import scala.xml.Elem

case class DefaultCard(color: String, number: Int, owner: Option[Player] = None)
  extends Card(owner) with Ordered[DefaultCard] {
  require(number >= 1 && number <= 13)
  require(color == "blue" || color == "red" || color == "yellow" || color == "green")

  def hasColor: Boolean = true

  def isWizard: Boolean = false

  def isJester: Boolean = false

  override def getStringRep: String = color + " " + number // TODO: als toString - mach Probleme mit dem Überschreiben der abstrakten Klasse - evt so belassen?

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

    val number = (node \ "number").text.trim.toInt

    var owner: Option[Player] = None
    if ((node \ "owner" ).text.trim != "None") {
      val player = Player.fromXML((node \ "owner").head.child.filter(node => node.text.trim != "").head)
      owner = Some(player)
    }

    this.copy(color = color, number = number, owner = owner)
  }

  override def toJson: JsValue = Json.obj(
    "type" -> "Default",
    "color" -> color,
    "number" -> number,
    "owner" -> ownerString
  )

  override def fromJson(jsValue: JsValue): Card = {
    val color = (jsValue \ "color").get.as[String]
    val number = (jsValue \ "number").get.as[Int]
    var owner: Option[Player] = None
    val ownerString = (jsValue \ "owner").get.as[String]
    if (ownerString != "None") owner = Some(Player(ownerString))

    this copy(color, number, owner)
  }
}
