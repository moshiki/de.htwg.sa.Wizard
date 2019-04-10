package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player
import org.scalatest.{Matchers, WordSpec}

class JesterCardSpec extends WordSpec with Matchers{
  "A Card" when {
    "is Jester" should {
      val jesterCard = JesterCard(null)
      "is a jester" in{
        jesterCard.isJester should be(true)
      }
      "is no wizard" in {
        jesterCard.isWizard should be(false)
      }
      "has no color" in {
        jesterCard.hasColor should be(false)
      }
      "has no player as owner" in {
        jesterCard.hasOwner should be(false)
      }

      val jesterCardWithPlayer = JesterCard(Player("TestPlayer"))
      "has an owner" in {
        jesterCardWithPlayer.hasOwner should be(true)
      }
      "has owner 'TestPlayer'" in {
        jesterCardWithPlayer.owner.name should be("TestPlayer")
      }
    }
  }
}
