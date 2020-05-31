package de.htwg.se.wizard.aview

import de.htwg.se.wizard.controller.controllerComponent.ControllerInterface
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TUISpec extends AnyWordSpec with Matchers with MockFactory {
  "A Wizard Tui" should {
    "register itself in the controller" in {
      val controllerStub = stub[ControllerInterface]
      (controllerStub.add _).when(*)

      val tui = new TUI(controllerStub)
      (controllerStub.add _).verify(tui)
    }

    "do nothing on input 'q'" in {
      val controllerStub = stub[ControllerInterface]
      (controllerStub.add _).when(*)
      val tui = new TUI(controllerStub)

      tui.processInput("q")
    }

    "do undo on input 'z'" in {
      val controllerMock = mock[ControllerInterface]
      (controllerMock.add _).expects(*)
      val tui = new TUI(controllerMock)
      (controllerMock.undo _).expects()

      tui.processInput("z")
    }

    "do redo on input 'y'" in {
      val controllerMock = mock[ControllerInterface]
      (controllerMock.add _).expects(*)
      val tui = new TUI(controllerMock)
      (controllerMock.redo _).expects()

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
      val expectedString = "3"
      val controllerMock = mock[ControllerInterface]
      (controllerMock.add _).expects(*)
      val tui = new TUI(controllerMock)
      (controllerMock.eval _).expects(expectedString)

      tui.processInput(expectedString)
    }
  }
}
