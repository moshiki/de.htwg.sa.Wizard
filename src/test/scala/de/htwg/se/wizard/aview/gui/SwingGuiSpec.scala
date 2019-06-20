package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.maincontroller._
import de.htwg.se.wizard.model.modelComponent.cards.{Card, CardStack, WizardCard}
import de.htwg.se.wizard.model.modelComponent.{Player, ResultTable}
import org.scalatest.{Matchers, WordSpec}

class SwingGuiSpec extends WordSpec with Matchers{

  val controller = new Controller(RoundManager(resultTable = ResultTable.initializeTable(),
    playerInterface = Player, cardInterface = Card, shuffledCardStack = CardStack.shuffleCards(CardStack.initialize)),
    playerInterface = Player, cardInterface = Card, staticResultTableInterface = ResultTable)
  "A SwingGuiSpec" should {
    "load the correct Panel" when {
      "Controller is in preSetupState" in {
        controller.state = PreSetupState(controller, Player, Card, ResultTable)
        SwingGui.getPanel(controller).isInstanceOf[WelcomePanel] should be(true)
      }

      "Controller is in SetupState" in {
        controller.state = SetupState(controller)
        SwingGui.getPanel(controller).isInstanceOf[PlayerSetupPanel] should be(true)
      }

      "Controller is in InGameState" in {
        controller.state = InGameState(controller)
        controller.roundManager = controller.roundManager.copy(players = List(Player("test", playerCards = Some(List(WizardCard())))))
        SwingGui.getPanel(controller).isInstanceOf[InGamePanel] should be(true)
      }

      "Controller is in GameOverState" in {
        controller.state = GameOverState(controller)
        SwingGui.getPanel(controller).isInstanceOf[GameOverPanel] should be(true)
      }
    }
  }

}
