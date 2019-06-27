package de.htwg.se.wizard.controller.fileIOComponent

import de.htwg.se.wizard.controller.maincontroller.{Controller, ControllerState, RoundManager}
import de.htwg.se.wizard.model.{ResultTableBuilderInterface, StaticCardInterface, StaticPlayerInterface}

trait FileIOInterface {
  def load(controller: Controller,
           staticPlayerInterface: StaticPlayerInterface,
           staticCardInterface: StaticCardInterface,
           resultTableBuilderInterface: ResultTableBuilderInterface): (ControllerState, RoundManager)
  def save (controller: Controller): Unit
}
