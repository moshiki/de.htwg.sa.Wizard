package de.htwg.sa.wizard.model.cardComponent.cardBaseImplementation

import de.htwg.sa.wizard.model.cardComponent.CardInterface
import play.api.libs.json.{JsValue, Json}

import scala.xml.Elem

case class DefaultCard(color: String, number: Int, owner: Option[String] = None)
  extends Card(owner) with Ordered[DefaultCard] with CardInterface {
  require(number >= 1 && number <= 13)
  require(color == "blue" || color == "red" || color == "yellow" || color == "green")

  def hasColor: Boolean = true

  def isWizard: Boolean = false

  def isJester: Boolean = false

  override def toString: String = color + " " + number

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
      <owner>{owner match {
        case Some(owner) => owner
        case None => "None"
      }}</owner>
    </DefaultCard>
  }

  def fromXML(node: scala.xml.Node): DefaultCard = {
    val color = (node \ "color").text.trim
    val number = (node \ "number").text.trim.toInt
    val owner = if ((node \ "owner" ).text.trim != "None") {
      Some((node \ "owner" ).text.trim)
    } else None
    this.copy(color = color, number = number, owner = owner)
  }

  override def toJson: JsValue = Json.obj(
    "type" -> "Default",
    "color" -> color,
    "number" -> number,
    "owner" -> ownerString
  )

  override def fromJson(jsValue: JsValue): CardInterface = {
    val color = (jsValue \ "color").get.as[String]
    val number = (jsValue \ "number").get.as[Int]
    val owner = if (ownerString != "None") Some(ownerString) else None
    this copy(color, number, owner)
  }
}
