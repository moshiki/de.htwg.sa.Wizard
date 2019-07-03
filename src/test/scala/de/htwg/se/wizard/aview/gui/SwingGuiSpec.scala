package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.maincontroller._
import de.htwg.se.wizard.model.modelComponent
import de.htwg.se.wizard.model.modelComponent.cards.{CardStack, WizardCard}
import de.htwg.se.wizard.model.modelComponent.{Player, ResultTable, RoundManager}
import org.scalatest.{Matchers, WordSpec}

class SwingGuiSpec extends WordSpec with Matchers{

  val controller = new Controller(RoundManager(resultTable = ResultTable.initializeTable(),
     shuffledCardStack = CardStack.shuffleCards(CardStack.initialize)))
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
        controller.roundManager = controller.roundManager.asInstanceOf[modelComponent.RoundManager].copy(players = List(Player("test", playerCards = Some(List(WizardCard())))))
        SwingGui.getPanel(controller).isInstanceOf[InGamePanel] should be(true)
      }

      "Controller is in GameOverState" in {
        controller.state = GameOverState(controller)
        SwingGui.getPanel(controller).isInstanceOf[GameOverPanel] should be(true)
      }
    }
  }

}
