package de.htwg.se.wizard.model

import de.htwg.se.wizard.Wizard
import org.scalatest.{Matchers, WordSpec}

class WizardSpec extends WordSpec with Matchers{
  "An initialized Game" should {
    "only allow 3 to 6 players" in {
      for (i <- 3 to 6) {
        val playerCount = Wizard.numberOfPlayers(i)
        playerCount should be >= 3
        playerCount should be <= 6
      }
    }
    "throw an Exception if to many or less players are wanted" in {
      an [IllegalArgumentException] should be thrownBy(Wizard.numberOfPlayers(2))
      an [IllegalArgumentException] should be thrownBy(Wizard.numberOfPlayers(7))
    }
  }
}
