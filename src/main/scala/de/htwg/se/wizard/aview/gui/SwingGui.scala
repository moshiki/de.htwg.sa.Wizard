package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.{Controller, GameOverState, InGameState, PreSetupState, SetupState}
import de.htwg.se.wizard.util.Observer

import scala.swing._

class SwingGui(controller: Controller) extends Frame with Observer {
  controller.add(this)

  title = "Wizard"

  contents = new WelcomePanel(controller)

  visible = true
  pack()

  override def update(): Unit = {
    contents = controller.state match {
      case _:PreSetupState => new WelcomePanel(controller)
      case _:SetupState => new PlayerSetupPanel(controller)
      case _:InGameState => new InGamePanel(controller)
      case _:GameOverState => new GameOverPanel(controller)
    }

    repaint()
  }
}
