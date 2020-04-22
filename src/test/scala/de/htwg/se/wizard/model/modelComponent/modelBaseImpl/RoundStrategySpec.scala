package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RoundStrategySpec extends AnyWordSpec with Matchers{
  "A RoundStrategy" when {
    "should set rounds based on players input" should {
      val roundManager1 = RoundStrategy.execute(3)
      "with three players" in {
        roundManager1.numberOfPlayers should be(3)
        roundManager1.numberOfRounds should be(20)
      }
      val roundManager2 = RoundStrategy.execute(4)
      "with four Players" in {
        roundManager2.numberOfPlayers should be(4)
        roundManager2.numberOfRounds should be (15)
      }
      val roundManager3 = RoundStrategy.execute(5)
      "with five Players" in {
        roundManager3.numberOfPlayers should be(5)
        roundManager3.numberOfRounds should be(12)
      }
    }


  }

}
