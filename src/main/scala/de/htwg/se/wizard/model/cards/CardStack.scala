package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player

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

  def getPlayerOfHighestCard(cardList: List[Card], color: String): Player = {
    val wizardCards = cardList.filter(card => card.isWizard).map(card => card.asInstanceOf[WizardCard])
    val defaultCards = cardList.filterNot(card => card.isWizard || card.isJester)
      .map(card => card.asInstanceOf[DefaultCard]).sortWith(_ > _)
    val jesterCards = cardList.filter(card => card.isJester).map(card => card.asInstanceOf[JesterCard])

    if (wizardCards.nonEmpty) wizardCards.head.owner.get
    else null
  }
}
