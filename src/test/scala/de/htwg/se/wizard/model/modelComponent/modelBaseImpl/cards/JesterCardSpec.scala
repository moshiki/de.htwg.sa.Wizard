package de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards

import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.Player
import org.scalatest.{Matchers, WordSpec}

class JesterCardSpec extends WordSpec with Matchers {
  "A JesterCard" when {
    "is a JesterCard without owner" should {
      val jesterCard = JesterCard()
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
        jesterCard.ownerName should be("unknown")
      }
      "is able to store itself in an xml representation and restore successfully" in {
        val xml = jesterCard.toXML
        val newCard = jesterCard.fromXML(xml)
        newCard should be(jesterCard)
      }
    }

    "is a JesterCard with owner 'TestPlayer'" should {
      val jesterCardWithPlayer = JesterCard(Some(Player("TestPlayer")))
      "has an owner" in {
        jesterCardWithPlayer.hasOwner should be(true)
      }
      "has owner 'TestPlayer'" in {
        jesterCardWithPlayer.ownerName should be("TestPlayer")
      }
      "is able to store itself in an xml representation and restore successfully" in {
        val xml = jesterCardWithPlayer.toXML
        val newCard = jesterCardWithPlayer.fromXML(xml)
        newCard should be(jesterCardWithPlayer)
      }
    }

    "should set Owner correctly" in {
      val jesterCardWithoutOwner = JesterCard()
      val testPlayer = Player("TestPlayer")
      val cardWithOwner = Card.setOwner(jesterCardWithoutOwner, testPlayer)
      cardWithOwner.ownerName should be("TestPlayer")
    }

    "has the right String representation" should {
      val jesterCard = JesterCard()
      "Have a nice String representation" in {
        jesterCard.toString should be("cards/Jester")
      }
    }
  }
}
