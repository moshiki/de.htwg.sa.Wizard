package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.{Controller, GameOverState, InGameState, PreSetupState, RoundManager, SetupState}
import de.htwg.se.wizard.model.cards.WizardCard
import de.htwg.se.wizard.model.{Player, ResultTable}
import org.scalatest.{Matchers, WordSpec}

class SwingGuiSpec extends WordSpec with Matchers{
  val controller = new Controller(RoundManager(resultTable = ResultTable(points = ResultTable.initializeVector())))
  val gui = new SwingGui(controller)
  "A SwingGuiSpec" should {
    "load the correct Panel" when {
      "Controller is in preSetupState" in {
        controller.state = PreSetupState(controller)
        gui.update()
        gui.contents contains new WelcomePanel(controller)
      }

      "Controller is in SetupState" in {
        controller.state = SetupState(controller)
        gui.update()
        gui.contents contains new PlayerSetupPanel(controller)
      }

      "Controller is in InGameState" in {
        controller.state = InGameState(controller)
        controller.roundManager = controller.roundManager.copy(players = List(Player("test", playerCards = Some(List(WizardCard())))))
        gui.update()
        gui.contents contains new InGamePanel(controller)
      }

      "Controller is in GameOverState" in {
        controller.state = GameOverState(controller)
        gui.update()
        gui.contents contains new GameOverPanel(controller)
      }
    }
  }

}
