package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.Controller
import javax.swing.BorderFactory

import scala.swing._
import scala.swing.event.ButtonClicked

class WelcomePanel(controller: Controller) extends BoxPanel(Orientation.Vertical){
  border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
  val threePlayerButton = new Button("3 Player")
  val fourPlayerButton = new Button("4 Player")
  val fivePlayerButton = new Button("5 Player")

  contents += new Label("How many Players?")
  contents += threePlayerButton
  contents += fourPlayerButton
  contents += fivePlayerButton

  listenTo(threePlayerButton)
  listenTo(fourPlayerButton)
  listenTo(fivePlayerButton)

  reactions += {
    case ButtonClicked(`threePlayerButton`) => controller.eval("3")
    case ButtonClicked(`fourPlayerButton`) => controller.eval("4")
    case ButtonClicked(`fivePlayerButton`) => controller.eval("5")
  }
}
