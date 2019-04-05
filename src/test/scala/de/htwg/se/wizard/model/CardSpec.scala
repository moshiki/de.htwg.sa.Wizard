package de.htwg.se.wizard.model

import org.scalatest.{Matchers, WordSpec}

class CardSpec extends WordSpec with Matchers{
  "A Card" when {
    "is Jester" should {
      val jesterCard = Card(null, 0, null)
      "have value 0" in{
        jesterCard.number should be(0)
      }
      "is set" in{
        jesterCard.isJester should be(true)
      }
    }
  }
}
