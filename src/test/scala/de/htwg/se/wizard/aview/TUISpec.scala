package de.htwg.se.wizard.aview

import de.htwg.se.wizard.controller.maincontroller.{Controller, RoundManager}
import de.htwg.se.wizard.model.modelComponent.cards.StaticCard
import de.htwg.se.wizard.model.modelComponent.{ResultTableBuilder, StaticPlayer}
import org.scalatest.{Matchers, WordSpec}

class TUISpec extends WordSpec with Matchers {
  "A Wizard Tui" should {
    val cardInterface = StaticCard()
    val playerInterface = StaticPlayer()
    val controller = new Controller(RoundManager(numberOfPlayers = 3, resultTable = ResultTableBuilder().initializeTable(),
      staticPlayerInterface = playerInterface, staticCardInterface = cardInterface),
      staticCardInterface = cardInterface, staticPlayerInterface = playerInterface, resultTableBuilderInterface = ResultTableBuilder())
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
