package de.htwg.se.wizard.model

import de.htwg.se.wizard.controller.Controller
import org.scalatest.{Matchers, WordSpec}

class RoundManagerSpec extends WordSpec with Matchers {
  "A Round Manager" when {
    "new" should {
      val roundManager = RoundManager()
      val controller = new Controller(roundManager)
      "set the number of rounds to play to 0" in {
        roundManager.roundsForThisGame should be(0)
      }
      "set the number of players correctly and trigger the next controller state" in {
        val oldState = controller.state
        roundManager.setNumberOfPlayers(3)
        roundManager.numberOfPlayers should be(3)
        controller.state should not equal oldState
      }
    }
    "has the number of players initialized" should {
      val roundManager = RoundManager()
      "calculate the number of rounds to play correctly" in {
        roundManager.numberOfPlayers = 3
        roundManager.roundsForThisGame should be(20)

        roundManager.numberOfPlayers = 4
        roundManager.roundsForThisGame should be(15)

        roundManager.numberOfPlayers = 5
        roundManager.roundsForThisGame should be(12)

        roundManager.numberOfPlayers = 6
        an[IllegalArgumentException] shouldBe thrownBy(roundManager.roundsForThisGame)
      }
    }
    "controller is in setup mode" should {
      val roundManager = RoundManager()
      val controller = new Controller(roundManager)
      "ask for the next player's name correctly" in {
        roundManager.numberOfPlayers = 3
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
      "trigger the next state when enough players enterd their names" in {
        val oldState = controller.state
        roundManager.addPlayer("Player 2")
        roundManager.addPlayer("Player 3")
        controller.state should not equal oldState
      }
    }
    "controller is in game mode" should {
      val roundManager = RoundManager()
      val controller = new Controller(roundManager)
      roundManager.numberOfPlayers = 3
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
      "return the correct state string" in {
        roundManager.players = List(Player("Name"))
        roundManager.currentPlayer = 2
        roundManager.getPlayerStateStrings should startWith(
        """Round 1 - Player: Name
Select one of the following cards:""".stripMargin)
      }
      "trigger the next state and return game over when game is over" in {
        val oldState = controller.state
        roundManager.currentPlayer = 2
        roundManager.currentRound = 20
        roundManager.getPlayerStateStrings should be("\nGame Over! Press 'q' to quit.")
        controller.state should not equal oldState
      }
    }
  }
}
