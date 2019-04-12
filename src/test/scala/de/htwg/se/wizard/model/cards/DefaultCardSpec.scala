package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player
import org.scalatest.{Matchers, WordSpec}

class DefaultCardSpec extends WordSpec with Matchers {
  "A Card" when {
    "is a blue DefaultCard of value 2 without owner" should {
      val defaultCard = DefaultCard("blue", 2, null)

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
      }
    }

    "is a blue DefaultCard of value 2 with owner 'TestPlayer" should {
      val defCardWithOwner = DefaultCard("blue", 2, Player("TestPlayer"))
      "has owner" in {
        defCardWithOwner.hasOwner should be(true)
      }
      "has owner 'TestPayer" in {
        defCardWithOwner.owner.name should be("TestPlayer")
      }
    }

    "is not a valid DefaultCard" should {
      "throws IllegalArgumentsException for number greater than 13" in {
        an [IllegalArgumentException] should be thrownBy DefaultCard("blue", 14, null)
      }
      "throws IllegalArgumentsException for number less than 1" in {
        an [IllegalArgumentException] should be thrownBy DefaultCard("blue", 0, null)
      }

      "throws IllegalArgumentException for wrong Color" in {
        an [IllegalArgumentException] should be thrownBy DefaultCard("purple", 2, null)
      }
    }

    "has the right String representation" should {
      val defCard = DefaultCard("blue", 2, Player("TestPlayer"))
      "Have a nice String representation" in {
        defCard.toString should be("blue 2")
      }
    }
  }
}
