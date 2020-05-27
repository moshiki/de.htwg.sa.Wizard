package de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CardStackSpec extends AnyWordSpec with Matchers {
  "An initial CardStack" should {
    val cardStack = CardStack()
    "contain 60 cards" in {
      cardStack.cards.size should be(60)
    }
    "contain 4 wizards" in {
      cardStack.cards.count(_.isWizard) should be(4)
    }
    "contain 4 jesters" in {
      cardStack.cards.count(_.isJester) should be(4)
    }
    "have 52 normal cards" in {
      cardStack.cards.count(_.isInstanceOf[DefaultCard]) should be(52)
    }

    val defaultCards = cardStack.cards.filter(_.isInstanceOf[DefaultCard]).map(_.asInstanceOf[DefaultCard])
    "have 13 red cards" in {
      defaultCards.count(_.color == "red") should be(13)
    }
    "have 13 blue cards" in {
      defaultCards.count(_.color == "blue") should be(13)
    }
    "have 13 yellow cards" in {
      defaultCards.count(_.color == "yellow") should be(13)
    }
    "have 13 green cards" in {
      defaultCards.count(_.color == "green") should be(13)
    }

    "have 4 cards of value 1 to 13" in {
      for (i <- 1 to 13) {
        defaultCards.count(_.number == i) should be(4)
      }
    }
  }

  "A shuffled CardStack" should {
    val cardStack = CardStack()
    val shuffledCardStack = cardStack.shuffleCards()

    "contains 60 cards" in {
      shuffledCardStack.cards.size should be(60)
    }
    "contains 4 Wizards" in {
      shuffledCardStack.cards.count(_.isWizard) should be(4)
    }
    "contains 4 Jesters" in {
      shuffledCardStack.cards.count(_.isJester) should be(4)
    }
    "has 52 normal cards" in {
      shuffledCardStack.cards.count(_.isInstanceOf[DefaultCard]) should be(52)
    }
    val shuffledDefaultcards = shuffledCardStack.cards.filter(_.isInstanceOf[DefaultCard]).map(_.asInstanceOf[DefaultCard])
    "has 13 red cards" in {
      shuffledDefaultcards.count(_.color == "red") should be(13)
    }
    "has 13 blue cards" in {
      shuffledDefaultcards.count(_.color == "blue") should be(13)
    }
    "has 13 yellow cards" in {
      shuffledDefaultcards.count(_.color == "yellow") should be(13)
    }
    "has 13 green cards" in {
      shuffledDefaultcards.count(_.color == "green") should be(13)
    }
    "has 4 cards of value 1 to 13" in {
      for (i <- 1 to 13) {
        shuffledDefaultcards.count(_.number == i) should be(4)
      }
    }
  }
  "A CardStack that determines the owner of the highest card" when {
    "there are different default cards" in {
      val cardList = List(DefaultCard("blue", 12, Some("Olaf")), DefaultCard("red", 13, Some("Tim")))
      CardStack.playerOfHighestCard(cardList, Some("green")) should be(Some("Tim"))
    }
    "there is a wizardCard in the stack" in {
      val cardList = List(WizardCard(Some("Olaf")), DefaultCard("red", 13, Some("Tim")))
      CardStack.playerOfHighestCard(cardList, Some("green")) should be(Some("Olaf"))
    }
    "there are only wizardCards in the stack" in {
      val cardList = List(WizardCard(Some("Olaf")), WizardCard(Some("Tim")))
      CardStack.playerOfHighestCard(cardList, Some("green")) should be(Some("Olaf"))
    }
    "there are two DefaultCards of same value" in {
      val cardList = List(DefaultCard("blue", 13, Some("Olaf")), DefaultCard("red", 13, Some("Tim")))
      CardStack.playerOfHighestCard(cardList, Some("green")) should be(Some("Olaf"))
    }
    "there are two cards of same value, but one is in the trump color" in {
      val cardList = List(DefaultCard("blue", 13, Some("Olaf")), DefaultCard("red", 13, Some("Tim")))
      CardStack.playerOfHighestCard(cardList, Some("red")) should be(Some("Tim"))
    }
    "there are only jesterCards in the stack" in {
      val cardList = List(JesterCard(Some("Olaf")), JesterCard(Some("Tim")))
      CardStack.playerOfHighestCard(cardList, Some("green")) should be(Some("Olaf"))
    }
  }
}
