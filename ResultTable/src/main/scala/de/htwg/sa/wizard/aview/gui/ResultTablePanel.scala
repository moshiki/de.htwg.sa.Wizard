package de.htwg.sa.wizard.aview.gui

import de.htwg.sa.wizard.controller.controllerComponent.ResultTableControllerInterface

import scala.swing.{ScrollPane, Table}

class ResultTablePanel(controllerInterface: ResultTableControllerInterface) extends ScrollPane {
  contents = new Table(controllerInterface.pointArrayForView, controllerInterface.playerList)
}
