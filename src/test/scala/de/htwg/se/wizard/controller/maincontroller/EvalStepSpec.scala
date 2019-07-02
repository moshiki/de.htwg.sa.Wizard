package de.htwg.se.wizard.controller.maincontroller

import de.htwg.se.wizard.model.modelComponent.{ResultTable, RoundManager}
import org.scalatest.{Matchers, WordSpec}

class EvalStepSpec extends WordSpec with Matchers {
  "An EvalStep" when {
    val controller = new Controller(RoundManager(resultTable = ResultTable.initializeTable()))
    val evalStep = new EvalStep(controller)
    "saves the current controller's state and round manager" in {
      val state = (controller.roundManager, controller.state)
      evalStep.doStep()
      evalStep.memento should be(state)
    }

    "restore the previous state and save the current one" in {
      val oldState = (controller.roundManager.copy(), controller.state)
      controller.state = InGameState(controller)
      val newState = (controller.roundManager.copy(), controller.state)
      evalStep.undoStep()
      evalStep.memento should be(newState)
      controller.roundManager should be(oldState._1)
      controller.state should be(oldState._2)
    }

    "revert the undo command" in {
      val afterUndoState = (controller.roundManager.copy(), controller.state)
      val beforeUndoState = evalStep.memento
      evalStep.redoStep()
      evalStep.memento should be(afterUndoState)
      controller.roundManager should be(beforeUndoState._1)
      controller.state should be(beforeUndoState._2)
    }
  }
}
