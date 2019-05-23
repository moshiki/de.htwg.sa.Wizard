package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player
import org.scalatest.{Matchers, WordSpec}

class CardStackSpec extends WordSpec with Matchers {
  "An initial CardStack" should {
    val cardStack = CardStack.initialize
    "contain 60 cards" in {
      cardStack.size should be(60)
    }
    "contain 4 wizards" in {
      cardStack.count(_.isWizard) should be(4)
    }
    "contain 4 jesters" in {
      cardStack.count(_.isJester) should be(4)
    }
    "have 52 normal cards" in {
      cardStack.count(_.isInstanceOf[DefaultCard]) should be(52)
    }

    val defaultCards = cardStack.filter(_.isInstanceOf[DefaultCard]).map(_.asInstanceOf[DefaultCard])
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
    val cardStack = CardStack.initialize
    val shuffledCardStack = CardStack.shuffleCards(cardStack)

    "contains 60 cards" in {
      shuffledCardStack.size should be(60)
    }
    "contains 4 Wizards" in {
      shuffledCardStack.count(_.isWizard) should be(4)
    }
    "contains 4 Jesters" in {
      shuffledCardStack.count(_.isJester) should be(4)
    }
    "has 52 normal cards" in {
      shuffledCardStack.count(_.isInstanceOf[DefaultCard]) should be(52)
    }
    val shuffledDefaultcards = shuffledCardStack.filter(_.isInstanceOf[DefaultCard]).map(_.asInstanceOf[DefaultCard])
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
      val cardList = List(DefaultCard("blue", 12, Some(Player("Olaf"))), DefaultCard("red", 13, Some(Player("Tim"))))
      CardStack.getPlayerOfHighestCard(cardList, Some("green")) should be(Player("Tim"))
    }
    "there is a wizardCard in the stack" in {
      val cardList = List(WizardCard(Some(Player("Olaf"))), DefaultCard("red", 13, Some(Player("Tim"))))
      CardStack.getPlayerOfHighestCard(cardList, Some("green")) should be(Player("Olaf"))
    }
    "there are only wizardCards in the stack" in {
      val cardList = List(WizardCard(Some(Player("Olaf"))), WizardCard(Some(Player("Tim"))))
      CardStack.getPlayerOfHighestCard(cardList, Some("green")) should be(Player("Olaf"))
    }
    "there are two DefaultCards of same value" in {
      val cardList = List(DefaultCard("blue", 13, Some(Player("Olaf"))), DefaultCard("red", 13, Some(Player("Tim"))))
      CardStack.getPlayerOfHighestCard(cardList, Some("green")) should be(Player("Olaf"))
    }
    "there are two cards of same value, but one is in the trump color" in {
      val cardList = List(DefaultCard("blue", 12, Some(Player("Olaf"))), DefaultCard("red", 13, Some(Player("Tim"))))
      CardStack.getPlayerOfHighestCard(cardList, Some("green")) should be(Player("Tim"))
    }
    "there are only jesterCards in the stack" in {
      val cardList = List(JesterCard(Some(Player("Olaf"))), JesterCard(Some(Player("Tim"))))
      CardStack.getPlayerOfHighestCard(cardList, Some("green")) should be(Player("Olaf"))
    }
  }
}
