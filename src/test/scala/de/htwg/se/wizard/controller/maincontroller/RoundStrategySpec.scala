package de.htwg.se.wizard.controller.maincontroller

import de.htwg.se.wizard.model.modelComponent.Player
import de.htwg.se.wizard.model.modelComponent.cards.Card
import org.scalatest.{Matchers, WordSpec}

class RoundStrategySpec extends WordSpec with Matchers{
  "A RoundStrategy" when {
    val cardInterface = Card
    val playerInterface = Player
    "should set rounds based on players input" should {
      val roundManager1 = RoundStrategy.execute(3,playerInterface = playerInterface, cardInterface = cardInterface )
      "with three players" in {
        roundManager1.numberOfPlayers should be(3)
        roundManager1.numberOfRounds should be(20)
      }
      val roundManager2 = RoundStrategy.execute(4,playerInterface = playerInterface, cardInterface = cardInterface)
      "with four Players" in {
        roundManager2.numberOfPlayers should be(4)
        roundManager2.numberOfRounds should be (15)
      }
      val roundManager3 = RoundStrategy.execute(5,playerInterface = playerInterface, cardInterface = cardInterface)
      "with five Players" in {
        roundManager3.numberOfPlayers should be(5)
        roundManager3.numberOfRounds should be(12)
      }
    }


  }

}
