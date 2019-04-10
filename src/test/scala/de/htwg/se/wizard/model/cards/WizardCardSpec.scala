package de.htwg.se.wizard.model.cards

import de.htwg.se.wizard.model.Player
import org.scalatest.{Matchers, WordSpec}

class WizardCardSpec extends WordSpec with Matchers {
  "A Card" when {
    "is a WizardCard" should {
      val wizardCard = WizardCard(null)
      "is a wizard" in {
        wizardCard.isWizard should be(true)
      }
      "is no jester" in {
        wizardCard.isJester should be(false)
      }
      "has no colour" in {
        wizardCard.hasColor should be(false)
      }
      "has no owner" in {
        wizardCard.hasOwner should be(false)
      }

      val wizardCardWithOwner = WizardCard(Player("TestPlayer"))
      "has owner" in {
        wizardCardWithOwner.hasOwner should be(true)
      }
      "has owner 'TestPlayer'" in {
        wizardCardWithOwner.owner.name should be ("TestPlayer")
      }
    }
  }
}
