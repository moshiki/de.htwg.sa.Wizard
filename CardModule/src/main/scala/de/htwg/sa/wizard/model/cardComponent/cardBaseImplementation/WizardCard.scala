package de.htwg.sa.wizard.model.cardComponent.cardBaseImplementation

import de.htwg.sa.wizard.model.cardComponent.CardInterface
import play.api.libs.json.{JsValue, Json}

import scala.xml.Elem

case class WizardCard(owner: Option[String] = None) extends Card(owner) with CardInterface {
  def hasColor: Boolean = false

  def isWizard: Boolean = true

  def isJester: Boolean = false

  override def toString: String = "Wizard"

  override def toXML: Elem = {
    <WizardCard>
      <owner>{owner match {
        case Some(owner) => owner
        case None => "None"
      }}</owner>
    </WizardCard>
  }

  def fromXML(node: scala.xml.Node): WizardCard = {
    val owner = if ((node \ "owner" ).text.trim != "None") {
      //val playerName = Player.fromXML((node \ "owner").head.child.filter(node => node.text.trim != "").head)
      Some((node \ "owner" ).text.trim)
    } else None
    this.copy(owner = owner)
  }

  override def toJson: JsValue = Json.obj(
    "type" -> "Wizard",
    "owner" -> ownerString
  )

  override def fromJson(jsValue: JsValue): CardInterface = {
    val owner = if (ownerString != "None") Some(ownerString) else None
    this copy owner
  }
}
