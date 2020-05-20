package de.htwg.sa.wizard.resultTable.gui

import de.htwg.sa.wizard.resultTable.controllerComponent.ResultTableControllerInterface

import scala.swing.{ScrollPane, Table}

class ResultTablePanel(controllerInterface: ResultTableControllerInterface) extends ScrollPane {
  contents = new Table(controllerInterface.pointArrayForView, controllerInterface.playerList)
}
