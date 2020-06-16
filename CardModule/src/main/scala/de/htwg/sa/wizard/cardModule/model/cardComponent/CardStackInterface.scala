package de.htwg.sa.wizard.cardModule.model.cardComponent

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.{DefaultCard, JesterCard, WizardCard}

trait CardStackInterface {
  val cards: List[CardInterface]
  def shuffleCards(): CardStackInterface
  def split(numberOfPlayers: Int, currentRound: Int): CardStackInterface
  def topOfCardStackString: String
  def playerOfHighestCard(cardList: List[CardInterface], color: Option[String]): String
}

/*
object CardStackInterface extends PlayJsonSupport {
  import play.api.libs.json._

  implicit val cardWrites: Writes[CardStackInterface] = {
    case cardStackInterface: CardStackInterface => cardStackInterface.cards.map(c => c.toJson).collect()
  }

  implicit val cardReads: Reads[CardStackInterface] = (json: JsValue) => {
    val cardType = (json \ "type").get.as[String]
    val cardOwner = (json \ "owner").asOpt[String]
    val card = cardType match {
      case "Wizard" => WizardCard(cardOwner).fromJson(json)
      case "Jester" => JesterCard(cardOwner).fromJson(json)
      case "Default" => DefaultCard("blue", 1, cardOwner).fromJson(json)
    }
    JsSuccess(card)
  }
}*/
