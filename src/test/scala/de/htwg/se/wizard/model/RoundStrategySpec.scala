package de.htwg.se.wizard.model

import org.scalatest.{Matchers, WordSpec}

class RoundStrategySpec extends WordSpec with Matchers{
  "A RoundStrategy" when {
    "should set rounds based on players input" should {
      val roundManager = RoundManager()
      val roundManager2 = RoundStrategy.execute(3,roundManager)
      "with three players" in {
        roundManager2.getNumberOfPlayers() should be(3)
        roundManager2.numberOfRounds should be(20)
      }
    }

  }

}
