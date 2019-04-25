package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards.WizardCard
import org.scalatest.{Matchers, WordSpec}

class CardStackSpec extends WordSpec with Matchers{
  "An initial CardStack" should {
    val cardStack = CardStack.initiaize
    "contain 4 wizards" in {
      cardStack.count(_.isWizard) should be(4)
    }
    "contain 4 jesters" in {
      cardStack.count(_.isJester) should be(4)
    }
  }
}
