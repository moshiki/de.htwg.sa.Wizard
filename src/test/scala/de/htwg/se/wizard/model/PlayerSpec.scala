package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards.JesterCard
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.ListBuffer

class PlayerSpec extends WordSpec with Matchers {

  "A Player" when {
    "new" should {
      val player = Player("Name")
      "have a name" in {
        player.name should be("Name")
      }
      "have a nice String representation" in {
        player.toString should be("Name")
      }
      "get the correct String for its turn" in {
        Player.playerTurn(player, 1) should startWith(
          """Round 1 - Player: Name
Select one of the following cards:""".stripMargin)
      }

      "get the correct String for stitches" in {
        player.playerCards = Some(ListBuffer(JesterCard(Some(player))))
        Player.playerPrediction(player, 3, Some("blue")) should be(
          """Round 3 - Player: Name
            |"Trump Color: "blue"
            |"Your Cards: "Jester"
            |Enter the amount of stitches you think you will get: """.stripMargin)
      }
    }
  }

}
