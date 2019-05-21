package de.htwg.se.wizard.model

import de.htwg.se.wizard.controller.Controller
import org.scalatest.{Matchers, WordSpec}

class RoundManagerSpec extends WordSpec with Matchers {
  "A Round Manager" when {
    "new" should {
      val roundManager = RoundManager()
      "set the number of rounds to play to 0" in {
        roundManager.roundsForThisGame should be(0)
      }
      "set the number of players correctly" in {
        val roundManager = RoundManager(3)
        roundManager.checkNumberOfPlayers(3)
        roundManager.numberOfPlayers should be(3)
      }
    }
    "has the number of players initialized" should {
      val roundManager1 = RoundManager(3)
      "with three Players" in {
        roundManager1.roundsForThisGame should be(20)
      }
      val roundManager2 = RoundManager(4)
      "with four Players" in {
        roundManager2.roundsForThisGame should be(15)
      }

        val roundManager3 = RoundManager(5)
      "with five Players" in {
        roundManager3.roundsForThisGame should be(12)
      }
      "invalid amount of Players" in {
        val roundManager4 = RoundManager(6)
        an[IllegalArgumentException] shouldBe thrownBy(roundManager4.roundsForThisGame)
      }
    }
    "controller is in setup mode" should {
      val roundManager = RoundManager(3)
      val controller = new Controller(roundManager)
      "ask for the next player's name correctly" in {
        roundManager.currentPlayer = 0
        roundManager.getSetupStrings should be("Player 1, please enter your name:")
      }
      "get the next player correctly" in {
        roundManager.currentPlayer = 0
        roundManager.nextPlayerSetup should be(1)
      }
      "increment the player count up to the number provided by the user" in {
        roundManager.currentPlayer = 2
        roundManager.nextPlayerSetup should be(3)
      }
      "reset the player count when there's no next player" in {
        roundManager.currentPlayer = 3
        roundManager.nextPlayerSetup should be(0)
      }
      "add a player correctly to a list of all players" in {
        roundManager.addPlayer("Name")
        roundManager.players should be(List(Player("Name")))
      }
      "trigger the next state when enough players entered their names" in {
        val oldState = controller.state
        roundManager.addPlayer("Player 2")
        roundManager.addPlayer("Player 3")
        controller.state should not equal oldState
      }
    }
    "controller is in game mode" should {
      val roundManager = RoundManager(3)
      val controller = new Controller(roundManager)
      "put the selected card on the middle stack" in {
        roundManager.evaluate(1)
        // TODO: finish test before doing the correct implementation
      }
      "get the next player correctly" in {
        roundManager.currentPlayer = 0
        roundManager.nextPlayer should be(1)
      }
      "reset the player counter once all players played a card" in {
        roundManager.currentPlayer = 2
        roundManager.nextPlayer should be(0)
      }
      "return the correct state string of reading in the prediction" in {
        roundManager.players = List(Player("Name"), Player("P2"))
        roundManager.currentPlayer = 2
        roundManager.getPlayerStateStrings should startWith(
        """Round 1 - Player: Name
Enter the amount of stitches you think you will get: """.stripMargin)
      }

      "update predictionPerRound correctly" in {
        roundManager.updatePlayerPrediction(3)
        roundManager.predictionPerRound should be(List(3))
      }

      "empty predictionPerRound once a new round starts " in {
        roundManager.currentPlayer = 0
        roundManager.updatePlayerPrediction(1)
        roundManager.predictionPerRound should be(List(1))
      }

      "set NextRoundB false and mod to zero if all players gave told prediction" in {
        roundManager.currentPlayer = 0
        roundManager.updatePlayerPrediction(1)
        roundManager.currentPlayer = 1
        roundManager.updatePlayerPrediction(2)
        roundManager.currentPlayer = 2
        roundManager.updatePlayerPrediction(3)
        roundManager.predictionPerRound should be(List(1,2,3))
        roundManager.nextRoundB should be(false)
        roundManager.mod should be(0)
      }

      "return the correct state string once all players told their prediction" in {
        roundManager.currentPlayer = 0
        roundManager.players = List(Player("Name"), Player("P2"))
        roundManager.getPlayerStateStrings should startWith(
          """Round 1 - Player: P2
Select one of the following cards:""".stripMargin)
      }

      "increase the current round when it's player ones turn again and set NextRoundB true" in {
        roundManager.currentPlayer = 2
        roundManager.currentRound = 1
        roundManager.getPlayerStateStrings
        roundManager.nextRoundB should be(true)
        roundManager.currentRound should be(2)
      }
      "not increase the current round when its not correct to do so" in {
        roundManager.currentPlayer = 0
        roundManager.currentRound = 1
        roundManager.getPlayerStateStrings
        roundManager.currentRound should be(1)
      }
      "trigger the next state and return game over when game is over" in {
        val oldState = controller.state
        roundManager.currentPlayer = 2
        roundManager.currentRound = 20
        roundManager.getPlayerStateStrings should be("\nGame Over! Press 'q' to quit.")
        controller.state should not equal oldState
      }

      "ask Player for his prediction" in {


      }
    }
  }
  "A RoundManager Builder" when {
    "builds a correct RoundManager" in {
      val roundManager = RoundManager.Builder().withNumberOfPlayers(3).withNumberOfRounds(20).build()
      roundManager should be(RoundManager(3, 20))
    }
  }
}
