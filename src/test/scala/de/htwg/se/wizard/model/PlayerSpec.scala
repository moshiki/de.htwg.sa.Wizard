package de.htwg.se.wizard.model

import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {

  "A Player" when {
    "new" should {
      val player = Player("Name", 0)
      "have a name" in {
        player.name should be("Name")
      }
      "have a nice String representation" in {
        player.toString should be("Name")
      }
      "get the correct String for its turn" in {
        Player.playerTurn(player, 1, CardStack.initialize) should startWith(
          """Round 1 - Player: Name
Select one of the following cards:""".stripMargin)
      }
      "get the correct String for stitches" in {
        Player.playerPrediction(player, 1) should startWith(
          """Round 1 - Player: Name
            |Enter the amount of stitches you think you will get:""".stripMargin)
      }
    }
  }

}
