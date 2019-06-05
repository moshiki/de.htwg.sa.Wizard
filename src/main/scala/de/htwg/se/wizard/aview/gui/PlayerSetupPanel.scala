package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.Controller

import scala.swing._
import scala.swing.event.ButtonClicked

class PlayerSetupPanel(controller: Controller) extends BoxPanel(Orientation.Vertical) {
  val currentPlayer:Int = controller.roundManager.currentPlayer
  val nameTextBox = new TextField()
  val nextButton = new Button("->")

  listenTo(nextButton)

  contents += new Label("Player " + currentPlayer + ", please enter your name:")
  contents += nameTextBox
  contents += nextButton

  reactions += {
    case ButtonClicked(`nextButton`) => controller.eval(nameTextBox.text)
  }
}
