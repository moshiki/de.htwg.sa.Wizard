package de.htwg.se.wizard.aview

import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.{ResultTable, RoundManager}
import org.scalatest.{Matchers, WordSpec}

class TUISpec extends WordSpec with Matchers {
  "A Wizard Tui" should {
    val controller = new Controller(RoundManager(numberOfPlayers = 3, resultTable = ResultTable.initializeTable()))
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

    "do redo on input 'y'" in {
      tui.processInput("y")
    }

    "save the current game on input 's' and load with 'l'" in {
      val roundManager = controller.roundManager
      tui.processInput("s")
      tui.processInput("l")
      controller.roundManager should be(roundManager)
    }

    "should let the controller evaluate the input" in {
      tui.processInput("3")
      controller.getCurrentStateAsString should be("Player 1, please enter your name:")
    }
  }
}
