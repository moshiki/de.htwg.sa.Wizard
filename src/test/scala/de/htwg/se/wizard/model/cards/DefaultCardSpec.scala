package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player
import org.scalatest.{Matchers, WordSpec}

class DefaultCardSpec extends WordSpec with Matchers {
  "A Card" when {
    "is a blue DefaultCard of value 2 without owner" should {
      val defaultCard = DefaultCard("blue", 2)

      "have color" in {
        defaultCard.hasColor should be(true)
      }
      "be no wizard" in {
        defaultCard.isWizard should be(false)
      }
      "be no jester" in {
        defaultCard.isJester should be(false)
      }
      "have a value of 2" in {
        defaultCard.number should be(2)
      }
      "have no owner" in {
        defaultCard.hasOwner should be(false)
        defaultCard.ownerName should be("unknown")
      }
    }

    "is a blue DefaultCard of value 2 with owner 'TestPlayer" should {
      val defCardWithOwner = DefaultCard("blue", 2, Some(Player("TestPlayer")))
      "has owner" in {
        defCardWithOwner.hasOwner should be(true)
      }
      "has owner 'TestPayer" in {
        defCardWithOwner.ownerName should be("TestPlayer")
      }
    }

    "should set Owner correctly" in {
      val defCardWithoutOwner = DefaultCard("blue", 2)
      val testPlayer = new Player("TestPlayer")
      val cardWithOwner = Card.setOwner(defCardWithoutOwner, testPlayer)
      cardWithOwner.ownerName should be("TestPlayer")

    }

    "is not a valid DefaultCard" should {
      "throws IllegalArgumentsException for number greater than 13" in {
        an[IllegalArgumentException] should be thrownBy DefaultCard("blue", 14)
      }
      "throws IllegalArgumentsException for number less than 1" in {
        an[IllegalArgumentException] should be thrownBy DefaultCard("blue", 0)
      }

      "throws IllegalArgumentException for wrong Color" in {
        an[IllegalArgumentException] should be thrownBy DefaultCard("purple", 2)
      }
    }

    "can be compared to another DefaultCard" should {
      val card = DefaultCard("red", 5)
      "be equal to another DefaultCard with same number, but different color" in {
        val equalCard = card.copy(color = "blue")
        equalCard == card should be(true)
        card equals equalCard should be(true)
      }
      "be equal to another DefaultCard with same number, but different owner" in {
        val equalCard = card.copy(owner = Some(Player("test")))
        card == equalCard should be(true)
        card equals equalCard should be(true)
      }
      "be more than another DefaultCard with a lower number" in {
        val lowerCard = DefaultCard("red", 4)
        card > lowerCard should be(true)
        card == lowerCard should be(false)
        card equals lowerCard should be(false)
      }
      "be less than another DefaultCard with a higher number" in {
        val higherCard = DefaultCard("red", 6)
        card < higherCard should be(true)
        card == higherCard should be(false)
        card equals higherCard should be(false)
      }
    }

    "can be compared with another object-type" should {
      "be false" in {
        val wizardCard = WizardCard()
        val defaultCard = DefaultCard("red", 2)
        //noinspection ComparingUnrelatedTypes
        defaultCard equals wizardCard should be (false)
        //noinspection ComparingUnrelatedTypes
        defaultCard == wizardCard should be(false)
      }
    }

    "has the right String representation" should {
      val defCard = DefaultCard("blue", 2)
      "Have a nice String representation" in {
        defCard.toString should be("cards/blue 2")
      }
    }
  }
}

