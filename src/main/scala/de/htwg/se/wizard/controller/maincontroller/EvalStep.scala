package de.htwg.se.wizard.controller.maincontroller

import de.htwg.se.wizard.model.modelComponent.RoundManager
import de.htwg.se.wizard.util.Command

class EvalStep(controller: Controller) extends Command {
  var memento: (RoundManager, ControllerState) = (controller.roundManager.copy(), controller.state)
  override def doStep(): Unit = memento = (controller.roundManager.copy(), controller.state)

  override def undoStep(): Unit = {
    val newMemento = (controller.roundManager.copy(), controller.state)
    controller.roundManager = memento._1
    controller.state = memento._2
    memento = newMemento
  }

  override def redoStep(): Unit = {
    val newMemento = (controller.roundManager.copy(), controller.state)
    controller.roundManager = memento._1
    controller.state = memento._2
    memento = newMemento
  }
}
