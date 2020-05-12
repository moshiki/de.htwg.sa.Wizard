package de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards

import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player

import scala.util.Random

object CardStack {
  val initialize: List[Card] = {
    val wizards = List.fill(4)(Card.apply("wizard"))
    val jesters = List.fill(4)(Card.apply("jester"))
    val colors = List("red", "blue", "yellow", "green")
    val normals = 1 to 13 flatMap(number => colors.map(color => DefaultCard(color, number)))
    wizards ::: jesters ::: normals.toList
  }

  def shuffleCards(a: List[Card]): List[Card] = Random.shuffle(a)

  def playerOfHighestCard(cardList: List[Card], color: Option[String]): Option[Player] = {
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
