package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.Controller
import de.htwg.se.wizard.util.Observer

import scala.swing._

class SwingGui(controller: Controller) extends Frame with Observer {
  controller.add(this)

  title = "Wizard"
  contents = new WelcomePanel(controller)

  visible = true
  pack()

  override def update(): Unit = repaint()
}
