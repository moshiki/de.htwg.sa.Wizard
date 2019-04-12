package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player
import org.scalatest.{Matchers, WordSpec}

class JesterCardSpec extends WordSpec with Matchers{
  "A JesterCard" when {
    "is a JesterCard without owner" should {
      val jesterCard = JesterCard(null)
      "is a jester" in {
        jesterCard.isJester should be(true)
      }
      "is no wizard" in {
        jesterCard.isWizard should be(false)
      }
      "has no colour" in {
        jesterCard.hasColor should be(false)
      }
      "has no owner" in {
        jesterCard.hasOwner should be(false)
      }
    }

    "is a JesterCard with owner 'TestPlayer'" should {
      val jesterCardWithPlayer = JesterCard(Player("TestPlayer"))
      "has an owner" in {
        jesterCardWithPlayer.hasOwner should be(true)
      }
      "has owner 'TestPlayer'" in {
        jesterCardWithPlayer.owner.name should be("TestPlayer")
      }
    }

    "has the right String representation" should {
      val jesterCard = JesterCard(Player("TestPlayer"))
      "Have a nice String representation" in {
        jesterCard.toString should be("Jester")
      }
    }
  }
}
