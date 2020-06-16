package de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation

import de.htwg.sa.wizard.cardModule.model.cardComponent.{CardInterface, CardStackInterface}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}

import scala.util.Random

case class CardStack(cards: List[CardInterface] = {
  Random.shuffle({
    val wizards = List.fill(4)(CardInterface.apply("WizardCard"))
    val jesters = List.fill(4)(CardInterface.apply("JesterCard"))
    val colors = List("red", "blue", "yellow", "green")
    val normals = 1 to 13 flatMap (number => colors.map(color => DefaultCard(color, number)))
    wizards ::: jesters ::: normals.toList
  })
}) extends CardStackInterface {

  override def shuffleCards(): CardStackInterface = this.copy()

  override def split(numberOfPlayers: Int, currentRound: Int): CardStackInterface = {
    this.copy(cards.splitAt((numberOfPlayers - 1) * currentRound + 1)._2)
  }

  override def topOfCardStackString: String = cards.head.toString

  override def playerOfHighestCard(cardList: List[CardInterface], color: Option[String]): String = {
    val actualColor = color match {
      case Some(color) => color
      case _ => ""
    }
    val wizardCards = cardList.filter(card => card.isWizard).map(card => card.asInstanceOf[WizardCard])
    val defaultCards = cardList.filterNot(card => card.isWizard || card.isJester)
      .map(card => card.asInstanceOf[DefaultCard]).sortWith(_ > _)
    val jesterCards = cardList.filter(card => card.isJester).map(card => card.asInstanceOf[JesterCard])

    if (wizardCards.nonEmpty) wizardCards.head.owner.get
    else if (defaultCards.nonEmpty) {
      val highestNumber = defaultCards.head.number
      val cardsWithHighestNumberInNormalCards = defaultCards.filter(_.number == highestNumber)
      val highestCardMatchingTrumpColor = cardsWithHighestNumberInNormalCards.filter(_.color == actualColor)
      if (highestCardMatchingTrumpColor.nonEmpty) highestCardMatchingTrumpColor.head.owner.getOrElse("")
      else cardsWithHighestNumberInNormalCards.head.owner.getOrElse("")
    } else jesterCards.head.owner.getOrElse("")
  }
  override def fromJson(jsValue: JsValue): CardStackInterface = jsValue.validate[CardStack] match {
    case e: JsError => println(s"Error parsing CardSTack from Json: ${JsError.toJson(e)}"); this.shuffleCards()
    case JsSuccess(value, path) => value
  }
  override def toJson: JsValue = Json.toJson(this)
}

object CardStack {

  import play.api.libs.json._
  implicit val cardStackFormat: OFormat[CardStack] = Json.format[CardStack]
  implicit val cardStackWrites: Writes[CardStack] = Json.writes[CardStack]
  implicit val cardStackReads: Reads[CardStack] = Json.reads[CardStack]

  def playerOfHighestCard(cardList: List[CardInterface], color: Option[String]): Option[String] = {
    val actualColor = color match {
      case Some(color) => color
      case _ => ""
    }
    val wizardCards = cardList.filter(card => card.isWizard).map(card => card.asInstanceOf[WizardCard])
    val defaultCards = cardList.filterNot(card => card.isWizard || card.isJester)
      .map(card => card.asInstanceOf[DefaultCard]).sortWith(_ > _)
    val jesterCards = cardList.filter(card => card.isJester).map(card => card.asInstanceOf[JesterCard])

    if (wizardCards.nonEmpty) wizardCards.head.owner
    else if (defaultCards.nonEmpty) {
      val highestNumber = defaultCards.head.number
      val cardsWithHighestNumberInNormalCards = defaultCards.filter(_.number == highestNumber)
      val highestCardMatchingTrumpColor = cardsWithHighestNumberInNormalCards.filter(_.color == actualColor)
      if (highestCardMatchingTrumpColor.nonEmpty) highestCardMatchingTrumpColor.head.owner
      else cardsWithHighestNumberInNormalCards.head.owner
    } else jesterCards.head.owner
  }
}
