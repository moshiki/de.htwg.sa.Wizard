package de.htwg.se.wizard

import de.htwg.se.wizard.aview.TUI
import de.htwg.se.wizard.aview.gui.SwingGui
import de.htwg.se.wizard.controller.maincontroller.{Controller, RoundManager}
import de.htwg.se.wizard.model.modelComponent.cards.Card
import de.htwg.se.wizard.model.modelComponent.{Player, ResultTable}

import scala.io.StdIn.readLine

object Wizard {
  val roundManager = RoundManager(resultTable = new ResultTable(points = Vector.empty), playerInterface = Player, cardInterface = Card)
  val controller = new Controller(roundManager, Player, Card)
  val tui = new TUI(controller)
  val gui = new SwingGui(controller)
  controller.notifyObservers()

  def main(args: Array[String]): Unit = {
    var input: String = ""
    do {
      input = readLine()
      tui.processInput(input)
    } while (input != "q")
  }
}
