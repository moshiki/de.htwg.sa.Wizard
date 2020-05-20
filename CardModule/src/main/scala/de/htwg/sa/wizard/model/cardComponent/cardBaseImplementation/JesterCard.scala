package de.htwg.sa.wizard.model.cardComponent.cardBaseImplementation

import de.htwg.sa.wizard.model.cardComponent.CardInterface
import play.api.libs.json.{JsValue, Json}

import scala.xml.Elem

case class JesterCard(owner: Option[String] = None) extends Card(owner) with CardInterface {
  def hasColor: Boolean = false

  def isWizard: Boolean = false

  def isJester: Boolean = true

  override def toString: String = "Jester"

  override def toXML: Elem = {
    <JesterCard>
      <owner>{owner match {
        case Some(owner) => owner
        case None => "None"
      }}</owner>
    </JesterCard>
  }

  def fromXML(node: scala.xml.Node): JesterCard = {
    val owner = if ((node \ "owner" ).text.trim != "None") {
      Some((node \ "owner" ).text.trim)
    } else None

    this.copy(owner = owner)
  }

  override def toJson: JsValue = Json.obj(
    "type" -> "Jester",
    "owner" -> ownerString
  )

  override def fromJson(jsValue: JsValue): CardInterface = {
    val owner = if (ownerString != "None") Some(ownerString) else None
    this copy owner
  }
}
