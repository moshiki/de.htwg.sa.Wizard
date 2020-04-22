package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.controllerComponent.ControllerInterface
import de.htwg.se.wizard.util.Observer
import javax.swing.WindowConstants

import scala.swing._

class SwingGui(controller: ControllerInterface) extends Frame with Observer {
  controller.add(this)

  title = "Wizard"

  contents = new WelcomePanel(controller)

  peer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  visible = true
  centerOnScreen()
  resizable = false
  pack()

  override def update(): Unit = {
    contents = SwingGui.getPanel(controller)

    repaint()
  }
}

object SwingGui {
  def getPanel(controller: ControllerInterface): Panel = {
    controller.controllerStateAsString match {
      case "PreSetupState" => new WelcomePanel(controller)
      case "SetupState" => new PlayerSetupPanel(controller)
      case "InGameState" => new InGamePanel(controller)
      case "GameOverState" => new GameOverPanel(controller)
    }
  }
}
