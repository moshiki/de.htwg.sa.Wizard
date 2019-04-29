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
  }
}
