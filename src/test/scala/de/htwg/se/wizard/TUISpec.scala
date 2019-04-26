package de.htwg.se.wizard.model

import de.htwg.se.wizard.{TUI, Wizard}
import org.scalatest.{Matchers, WordSpec}

class TUISpec extends WordSpec with Matchers{
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
      an [IllegalArgumentException] should be thrownBy tui.getNumberOfPlayers(2)
      an [IllegalArgumentException] should be thrownBy tui.getNumberOfPlayers(6)
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
