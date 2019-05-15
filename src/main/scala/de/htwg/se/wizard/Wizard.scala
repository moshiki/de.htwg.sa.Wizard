package de.htwg.se.wizard

import de.htwg.se.wizard.aview.TUI
import de.htwg.se.wizard.controller.Controller
import de.htwg.se.wizard.model.RoundManager

import scala.io.StdIn.readLine

object Wizard {
  val controller = new Controller(new RoundManager())
  val tui = new TUI(controller)
  controller.notifyObservers()

  def main(args: Array[String]): Unit = {
    var input: String = ""
    do {
      input = readLine()
      tui.processInput(input)
    } while (input != "q")
  }
}
