package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.Controller
import javax.swing.BorderFactory

import scala.swing._
import scala.swing.event.ButtonClicked

class WelcomePanel(controller: Controller) extends BoxPanel(Orientation.Vertical) {
  border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
  val myFont = new Font("Herculanum", java.awt.Font.PLAIN, 20)
  val threePlayerButton: Button = new Button("3 Players") {
    font = myFont
  }

  val fourPlayerButton: Button = new Button("4 Players"){
    font = myFont
  }

  val fivePlayerButton: Button = new Button("5 Players"){
    font = myFont
  }

  contents += new FlowPanel() {
    contents += new Label("How many Players?") {
      font = myFont
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
