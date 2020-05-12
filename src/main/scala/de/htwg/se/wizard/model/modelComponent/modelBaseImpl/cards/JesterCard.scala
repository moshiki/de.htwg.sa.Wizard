package de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards


import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player
import play.api.libs.json.{JsValue, Json}

import scala.xml.Elem

case class JesterCard(owner: Option[Player] = None) extends Card(owner) {
  def hasColor: Boolean = false

  def isWizard: Boolean = false

  def isJester: Boolean = true

  override def toString: String = "Jester"

  override def toXML: Elem = {
    <JesterCard>
      <owner>{owner match {
        case Some(player) => player.toXML
        case None => "None"
      }}</owner>
    </JesterCard>
  }

  def fromXML(node: scala.xml.Node): JesterCard = {
    val owner = if ((node \ "owner" ).text.trim != "None") {
      val player = Player.fromXML((node \ "owner").head.child.filter(node => node.text.trim != "").head)
      Some(player)
    } else None

    this.copy(owner = owner)
  }

  override def toJson: JsValue = Json.obj(
    "type" -> "Jester",
    "owner" -> ownerString
  )

  override def fromJson(jsValue: JsValue): Card = {
    val ownerString = (jsValue \ "owner").get.as[String]
    val owner = if (ownerString != "None") Some(Player(ownerString)) else None
    this copy owner
  }
}