package de.htwg.se.wizard.aview

import de.htwg.sa.wizard.model.resultTableComponent.ResultTableInterface
import de.htwg.se.wizard.controller.controllerComponent.ControllerInterface
import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TUISpec extends AnyWordSpec with Matchers with MockFactory {
  "A Wizard Tui" should {
    val fileIOStub = stub[FileIOInterface]
    val resultTableStub = stub[ResultTableInterface]
    val controller = new Controller(RoundManager(numberOfPlayers = 3), fileIOStub, resultTableStub)
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
      val controllerMock = mock[ControllerInterface]
      controllerMock.add _ expects *
      inSequence{
        (controllerMock.save _).expects()
        (controllerMock.load _).expects()
      }

      val saveLoadTui = new TUI(controllerMock)

      saveLoadTui.processInput("s")
      saveLoadTui.processInput("l")
    }

    "should let the controller evaluate the input" in {
      tui.processInput("3")
      controller.currentStateAsString should be("Player 1, please enter your name:")
    }
  }
}
