package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.Controller
import javax.swing.BorderFactory

import scala.swing._

class GameOverPanel(controller: Controller) extends BoxPanel(Orientation.Vertical) {
  border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
  val myFont = new Font("Herculanum", java.awt.Font.PLAIN, 20)

  contents += new FlowPanel(new Label("Game Over!") {
    font = myFont
  })
  contents += new FlowPanel(new Label("Thanks for Playing") {
    font = myFont
  })

  contents += Swing.RigidBox(new Dimension(0, 20))

  contents += new ScrollPane {
    contents = new Table(controller.roundManager.resultTable.toAnyArray, controller.roundManager.players)
  }
}
