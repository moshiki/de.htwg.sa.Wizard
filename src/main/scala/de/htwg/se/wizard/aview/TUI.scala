package de.htwg.se.wizard.aview

import de.htwg.se.wizard.controller.Controller
import de.htwg.se.wizard.util.Observer

class TUI(controller: Controller) extends Observer{
  controller.add(this)

  def processInput(input: String): Unit = {
    input match {
      case "q" =>
      case _ => controller.eval(input)
    }
  }

  override def update(): Unit = println(controller.getCurrentState)
}
