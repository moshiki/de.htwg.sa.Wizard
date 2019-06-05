package de.htwg.se.wizard.aview

import de.htwg.se.wizard.controller.{Controller, RoundManager}
import de.htwg.se.wizard.model.ResultTable
import org.scalatest.{Matchers, WordSpec}

class TUISpec extends WordSpec with Matchers {
  "A Wizard Tui" should {
    val controller = new Controller(RoundManager(numberOfPlayers = 3, resultTable = new ResultTable(points = Vector.empty)))
    val tui = new TUI(controller)
    "register itself in the controller" in {
        controller.subscribers.contains(tui) should be(true)
    }
    "do nothing on input 'q'" in {
      tui.processInput("q")
    }

    "do undo on input 'z'" in {
      tui.processInput("z")
    }

    "should let the controller evaluate the input" in {
      tui.processInput("3")
      controller.getCurrentStateAsString should be("Player 1, please enter your name:")
    }
  }
}
