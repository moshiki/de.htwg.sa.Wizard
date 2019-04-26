package de.htwg.se.wizard

import de.htwg.se.wizard.model.{CardStack, Player}
import org.scalatest.{Matchers, WordSpec}

class TUISpec extends WordSpec with Matchers {
  val tui = new TUI()
  "An initialized Game" should {
    "only allow 3 to 5 players" in {
      for (i <- 3 to 5) {
        val playerCount = tui.getNumberOfPlayers(i)
        playerCount should be >= 3
        playerCount should be <= 5
      }
    }
    "throw an Exception if to many or less players are wanted" in {
      an[IllegalArgumentException] should be thrownBy tui.getNumberOfPlayers(2)
      an[IllegalArgumentException] should be thrownBy tui.getNumberOfPlayers(6)
    }

    "get the correct String for each round" in {
      val players = IndexedSeq(Player("test1"), Player("test2"))
      val cardStack = CardStack.initialize

      tui.playerTurn(players, 1, 0, cardStack) should startWith
        """
           Round 1 - Player 1 (test1)
           Select one of the following cards:
        """.stripMargin

    }
  }
  "The number of Elements" should {
    "be 3" in {
      val test = Array("Lisa", "Hans", "Peter")
      val indexedSeq = tui.playerSetup(test)
      indexedSeq.length should be (3)
    }
  }


}
