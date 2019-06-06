package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.Controller

import scala.swing._

class GameOverPanel(controller: Controller) extends BoxPanel(Orientation.Vertical) {
  contents += new Label("Game Over!")
  contents += new Label("Thanks for Playing")

  contents += new ScrollPane {
    contents = new Table(controller.roundManager.resultTable.toAnyArray, controller.roundManager.players)
  }
}
