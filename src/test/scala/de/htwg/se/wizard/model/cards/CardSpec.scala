package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player
import org.scalatest.{Matchers, WordSpec}

class CardSpec extends WordSpec with Matchers {
  "A Card" when {
    "is Card" should {
      val card = DefaultCard("blue", 2, Player("Player"))

      "has Color" in {
        card.hasColor should be(true)
      }
      "is no Wizard" in {
        card.isWizard should be(false)
      }
      "is no Jester" in {
        card.isJester should be(false)
      }
      "number is 2" in {
        card.number should be(2)
      }


    }

  }

}
