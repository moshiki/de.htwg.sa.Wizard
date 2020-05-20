package de.htwg.se.wizard.aview.gui

import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.{CardStack, WizardCard}
import de.htwg.sa.wizard.resultTable.controllerComponent.ResultTableControllerInterface
import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl._
import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.{Player, RoundManager}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SwingGuiSpec extends AnyWordSpec with Matchers with MockFactory {
  val fileIOStub: FileIOInterface = stub[FileIOInterface]
  val resultTableControllerStub: ResultTableControllerInterface = stub[ResultTableControllerInterface]
  val controller = new Controller(RoundManager(
     shuffledCardStack = CardStack.shuffleCards(CardStack.initialize)), fileIOStub, resultTableControllerStub)
  "A SwingGuiSpec" should {
    "load the correct Panel" when {
      "Controller is in preSetupState" in {
        controller.state = PreSetupState(controller)
        SwingGui.getPanel(controller).isInstanceOf[WelcomePanel] should be(true)
      }

      "Controller is in SetupState" in {
        controller.state = SetupState(controller)
        SwingGui.getPanel(controller).isInstanceOf[PlayerSetupPanel] should be(true)
      }

      "Controller is in InGameState" in {
        controller.state = InGameState(controller)
        controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(players = List(Player("test", playerCards = List(WizardCard()))))
        SwingGui.getPanel(controller).isInstanceOf[InGamePanel] should be(true)
      }

      "Controller is in GameOverState" in {
        controller.state = GameOverState(controller)
        SwingGui.getPanel(controller).isInstanceOf[GameOverPanel] should be(true)
      }
    }
  }

}
