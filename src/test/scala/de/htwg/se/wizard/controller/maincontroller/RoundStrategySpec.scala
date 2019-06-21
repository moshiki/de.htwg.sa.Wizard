package de.htwg.se.wizard.controller.maincontroller

import de.htwg.se.wizard.model.modelComponent.cards.StaticCard
import de.htwg.se.wizard.model.modelComponent.{ResultTableBuilder, StaticPlayer}
import org.scalatest.{Matchers, WordSpec}

class RoundStrategySpec extends WordSpec with Matchers{
  "A RoundStrategy" when {
    val cardInterface = StaticCard()
    val playerInterface = StaticPlayer()
    "should set rounds based on players input" should {
      val roundManager1 = RoundStrategy.execute(3, playerInterface, cardInterface, ResultTableBuilder())
      "with three players" in {
        roundManager1.numberOfPlayers should be(3)
        roundManager1.numberOfRounds should be(20)
      }
      val roundManager2 = RoundStrategy.execute(4, playerInterface, cardInterface, ResultTableBuilder())
      "with four Players" in {
        roundManager2.numberOfPlayers should be(4)
        roundManager2.numberOfRounds should be (15)
      }
      val roundManager3 = RoundStrategy.execute(5, playerInterface, cardInterface, ResultTableBuilder())
      "with five Players" in {
        roundManager3.numberOfPlayers should be(5)
        roundManager3.numberOfRounds should be(12)
      }
    }


  }

}
