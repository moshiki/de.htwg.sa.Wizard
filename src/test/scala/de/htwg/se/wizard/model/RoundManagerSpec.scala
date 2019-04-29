package de.htwg.se.wizard.model

import org.scalatest.{Matchers, WordSpec}

class RoundManagerSpec extends WordSpec with Matchers{
  val roundManager = new RoundManager()
  "A Round Manager" when {
    "new" should {
      "set the number of rounds to play to 0" in {
        roundManager.roundsForThisGame should be(0)
      }
      "return the welcome message when asking for the current state" in {
        roundManager.currentStateToString should be(
          "Welcome to Wizard!\nPlease enter the number of Players[3-5]:"
        )
      }
    }
    "has the number of players initialized" should {
      "calculate the number of rounds to play correctly" in {
        roundManager.numberOfPlayers = 3
        roundManager.roundsForThisGame should be(20)

        roundManager.numberOfPlayers = 4
        roundManager.roundsForThisGame should be(15)

        roundManager.numberOfPlayers = 5
        roundManager.roundsForThisGame should be(12)

        roundManager.numberOfPlayers = 6
        an [IllegalArgumentException] shouldBe thrownBy(roundManager.roundsForThisGame)
      }
    }
    "in setup mode" should {
      "ask for the next player's name correctly" in {
        roundManager.getSetupStrings should be("Player 1, please enter your name:")
      }
      "get the next player correctly" in {
        roundManager.numberOfPlayers = 3
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
    }
    "in normal mode" should {
      "get the next player correctly" in {
        roundManager.numberOfPlayers = 3
        roundManager.currentPlayer = 0
        roundManager.nextPlayer should be(1)
      }
      "increment the player count up to one value less than the number provided by the user" in {
        roundManager.currentPlayer = 2
        roundManager.nextPlayer should be(0)
      }
      "get the current players round String when in game" in {
        roundManager.players = List(Player("Name"))
        roundManager.numberOfPlayers = 3
        roundManager.currentPlayer = 2
        roundManager.getPlayerStateStrings should startWith
        """
           Round 1 - Player 1 (test1)
           Select one of the following cards:
        """.stripMargin
      }
      "set the state to game over and get the related String" in {
        roundManager.numberOfPlayers = 3
        roundManager.currentPlayer = 2
        roundManager.currentRound = 20
        roundManager.getPlayerStateStrings should be("\nGame Over! Press 'q' to quit.")
        roundManager.gameOver should be(true)
      }
    }
  }
}
