package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player
import org.scalatest.{Matchers, WordSpec}

class DefaultCardSpec extends WordSpec with Matchers {
  "A Card" when {
    "is DefaultCard" should {
      val defaultCard = DefaultCard("blue", 2, null)

      "has color" in {
        defaultCard.hasColor should be(true)
      }
      "is no wizard" in {
        defaultCard.isWizard should be(false)
      }
      "is no jester" in {
        defaultCard.isJester should be(false)
      }
      "number is 2" in {
        defaultCard.number should be(2)
      }
      "has no owner" in {
        defaultCard.hasOwner should be(false)
      }

      val defCardWithOwner = DefaultCard("blue", 2, Player("TestPlayer"))
      "has owner" in {
        defCardWithOwner.hasOwner should be(true)
      }
      "has owner 'TestPayer" in {
        defCardWithOwner.owner.name should be("TestPlayer")
      }

      "throws IllegalArgumentsException for number greater than 13" in {
        an [AssertionError] should be thrownBy DefaultCard("blue", 14, null)
      }
      "throws IllegalArgumentsException for number less than 1" in {
        an [AssertionError] should be thrownBy DefaultCard("blue", 0, null)
      }

      "throws IllegalArgumentException for wrong Color" in {
        an [IllegalArgumentException] should be thrownBy DefaultCard("purple", 2, null)
      }

    }
  }
}
