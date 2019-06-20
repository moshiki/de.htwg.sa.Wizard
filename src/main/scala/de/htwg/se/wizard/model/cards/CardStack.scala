package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.modelComponent.Player

import scala.collection.mutable.ListBuffer
import scala.util.Random

object CardStack {
  val initialize: List[Card] = {
    val wizards = List.fill(4)(Card.apply("wizard"))
    val jesters = List.fill(4)(Card.apply("jester"))
    val normals = for {
      color <- List("red", "blue", "yellow", "green")
      number <- 1 to 13
    } yield DefaultCard(color, number)

    wizards ::: jesters ::: normals
  }

  def shuffleCards(a: List[Card]): List[Card] = {
    Random.shuffle(a)
  }

  def getPlayerOfHighestCard(cardList: List[Card], color: Option[String]): Player = {
    var actualColor = ""
    if (color.isDefined) actualColor = color.get
    val wizardCards = cardList.filter(card => card.isWizard).map(card => card.asInstanceOf[WizardCard])
    val defaultCards = cardList.filterNot(card => card.isWizard || card.isJester)
      .map(card => card.asInstanceOf[DefaultCard]).sortWith(_ > _)
    val jesterCards = cardList.filter(card => card.isJester).map(card => card.asInstanceOf[JesterCard])

    if (wizardCards.nonEmpty) wizardCards.head.owner.get
    else if (defaultCards.nonEmpty) {
      val highestNumber = defaultCards.head.number
      val cardsWithHighestNumberInNormalCards = defaultCards.filter(_.number == highestNumber)
      val highestCardMatchingTrumpColor = cardsWithHighestNumberInNormalCards.filter(_.color == actualColor)
      if (highestCardMatchingTrumpColor.nonEmpty) highestCardMatchingTrumpColor.head.owner.get
      else cardsWithHighestNumberInNormalCards.head.owner.get
    } else jesterCards.head.owner.get
  }
}
