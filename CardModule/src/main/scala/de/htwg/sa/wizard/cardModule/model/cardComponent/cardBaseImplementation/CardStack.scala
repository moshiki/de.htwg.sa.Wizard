package de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation

import de.htwg.sa.wizard.cardModule.model.cardComponent.{CardInterface, CardStackInterface}

import scala.util.Random

case class CardStack() extends CardStackInterface {
  val cards: List[CardInterface] = {
    Random.shuffle({
      val wizards = List.fill(4)(CardInterface.apply("WizardCard"))
      val jesters = List.fill(4)(CardInterface.apply("JesterCard"))
      val colors = List("red", "blue", "yellow", "green")
      val normals = 1 to 13 flatMap (number => colors.map(color => DefaultCard(color, number)))
      wizards ::: jesters ::: normals.toList
    })
  }

  def shuffleCards(): CardStackInterface = this.copy

}

object CardStack {
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
