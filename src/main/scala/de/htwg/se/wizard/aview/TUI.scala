package de.htwg.se.wizard.aview

import de.htwg.se.wizard.controller.ControllerInterface
import de.htwg.se.wizard.util.Observer

class TUI(controller: ControllerInterface) extends Observer{
  controller.add(this)

  def processInput(input: String): Unit = {
    input match {
      case "q" =>
      case "z" => controller.undo()
      case "y" => controller.redo()
      case "s" => controller.saveGameXML()
      case _ => controller.eval(input)
    }
  }

  override def update(): Unit = println(controller.getCurrentStateAsString)
}
