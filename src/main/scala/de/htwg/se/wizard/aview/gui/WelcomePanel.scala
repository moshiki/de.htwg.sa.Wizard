package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.ControllerInterface
import javax.swing.{BorderFactory, ImageIcon}

import scala.swing._
import scala.swing.event.ButtonClicked

class WelcomePanel(controller: ControllerInterface) extends BoxPanel(Orientation.Vertical) {
  background = new Color(0, 100, 0)
  border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
  val myFont = new Font("Herculanum", java.awt.Font.PLAIN, 20)
  val threePlayerButton: Button = new Button("3 Players") {
    font = myFont
  }

  val fourPlayerButton: Button = new Button("4 Players") {
    font = myFont
  }

  val fivePlayerButton: Button = new Button("5 Players") {
    font = myFont
  }

  contents += new FlowPanel() {
    contents += new Label {
      private val temp = new ImageIcon("src/main/resources/wizard_logo.png").getImage
      private val resize = temp.getScaledInstance(800, 500, java.awt.Image.SCALE_SMOOTH)
      icon = new ImageIcon(resize)
    }
  }

  contents += new FlowPanel() {
    contents += new Label("How many Players?") {
      font = new Font("Herculanum", java.awt.Font.PLAIN, 40)
    }
  }

  contents += new FlowPanel() {
    contents += threePlayerButton
    contents += fourPlayerButton
    contents += fivePlayerButton
  }

  listenTo(threePlayerButton)
  listenTo(fourPlayerButton)
  listenTo(fivePlayerButton)


  reactions += {
    case ButtonClicked(`threePlayerButton`) => controller.eval("3")
    case ButtonClicked(`fourPlayerButton`) => controller.eval("4")
    case ButtonClicked(`fivePlayerButton`) => controller.eval("5")
  }
}
