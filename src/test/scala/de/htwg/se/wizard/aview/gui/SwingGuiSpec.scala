package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.controllerComponent.ControllerInterface
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SwingGuiSpec extends AnyWordSpec with Matchers with MockFactory {
  val controllerStub: ControllerInterface = stub[ControllerInterface]
  "A SwingGuiSpec" should {
    "load the correct Panel" when {
      "Controller is in preSetupState" in {
        (controllerStub.controllerStateAsString _).when() returns "PreSetupState"
        SwingGui.getPanel(controllerStub).isInstanceOf[WelcomePanel] should be(true)
      }

      "Controller is in SetupState" in {
        (controllerStub.controllerStateAsString _).when() returns "SetupState"
        SwingGui.getPanel(controllerStub).isInstanceOf[PlayerSetupPanel] should be(true)
      }

      "Controller is in InGameState" in {
        (controllerStub.controllerStateAsString _).when() returns "InGameState"
        (controllerStub.currentPlayerString _).when() returns ""
        (controllerStub.predictionMode _).when() returns true
        (controllerStub.currentRound _).when() returns 0
        (controllerStub.currentPlayersCards _).when() returns Nil
        (controllerStub.topOfStackCardString _).when() returns ""
        (controllerStub.resultArray _).when() returns Array(Array())
        (controllerStub.playersAsStringList _).when() returns List()
        SwingGui.getPanel(controllerStub).isInstanceOf[InGamePanel] should be(true)
      }

      "Controller is in GameOverState" in {
        (controllerStub.controllerStateAsString _).when() returns "GameOverState"
        (controllerStub.resultArray _).when() returns Array(Array())
        (controllerStub.playersAsStringList _).when() returns List()
        SwingGui.getPanel(controllerStub).isInstanceOf[GameOverPanel] should be(true)
      }
    }
  }

}
