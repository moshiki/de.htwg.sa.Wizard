package de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.util.Command

class EvalStep(controller: Controller) extends Command {
  var memento: (ModelInterface, ControllerState) = (controller.roundManager, controller.state)
  override def doStep(): Unit = memento = (controller.roundManager, controller.state)

  override def undoStep(): Unit = {
    val newMemento = (controller.roundManager, controller.state)
    controller.roundManager = memento._1
    controller.state = memento._2
    memento = newMemento
  }

  override def redoStep(): Unit = {
    val newMemento = (controller.roundManager, controller.state)
    controller.roundManager = memento._1
    controller.state = memento._2
    memento = newMemento
  }
}
