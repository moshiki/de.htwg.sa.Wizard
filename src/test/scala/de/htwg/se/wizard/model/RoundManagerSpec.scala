package de.htwg.se.wizard.model

import org.scalatest.{Matchers, WordSpec}

class RoundManagerSpec extends WordSpec with Matchers {
  "A Round Manager" when {
    "new" should {
      val roundManager = new RoundManager()
      "set the number of rounds to play to 0" in {
        roundManager.roundsForThisGame should be(0)
      }
    }
    "has the number of players initialized" should {
      val roundManager = new RoundManager()
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
    "in setup mode" should {
      val roundManager = new RoundManager()
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
        roundManager.updatePlayers("Name")
        roundManager.players should be(List(Player("Name")))
      }
      "switch to normal mode once enough players entered their name" in {
        roundManager.players = List(Player("1"), Player("2"))
        roundManager.addPlayer("3")
        roundManager.needsSetup should be(false)
      }
    }
    "in normal mode" should {
      val roundManager = new RoundManager()
      roundManager.needsSetup = false
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
      "be in game over mode even after not quitting" in {
        roundManager.getPlayerStateStrings should be("\nGame Over! Press 'q' to quit.")
        roundManager.gameOver should be(true)
      }
    }
  }
}
