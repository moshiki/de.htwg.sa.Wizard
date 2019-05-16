package de.htwg.se.wizard.model

import org.scalatest.{Matchers, WordSpec}

class RoundStrategySpec extends WordSpec with Matchers{
  "A RoundStrategy" when {
    "should set rounds based on players input" should {
      val roundManager1 = RoundManager()
      val roundManager2 = RoundStrategy.execute(3,roundManager1)
      val roundManager3 = RoundStrategy.strategy3Players(roundManager1)
      "with three players" in {
        roundManager2.getNumberOfPlayers() should be(3)
        roundManager2.numberOfRounds should be(20)
        roundManager3 should equal(roundManager2)

      }
      val roundManager4 = RoundManager()
      val roundManager5 = RoundStrategy.execute(4,roundManager4)
      "with four Players" in {
        roundManager5.getNumberOfPlayers() should be(4)
        roundManager5.numberOfRounds should be (15)
      }
    }


  }

}
