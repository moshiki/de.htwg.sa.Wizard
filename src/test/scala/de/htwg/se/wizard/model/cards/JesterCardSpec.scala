package de.htwg.se.wizard.model.cards

import org.scalatest.{Matchers, WordSpec}

class JesterCardSpec extends WordSpec with Matchers{
  "A Card" when {
    "is Jester" should {
      val jesterCard = JesterCard(null)
      "is set" in{
        jesterCard.isJester should be(true)
      }
    }
  }
}
