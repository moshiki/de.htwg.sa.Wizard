package de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl

import de.htwg.sa.wizard.model.resultTableComponent.ResultTableInterface
import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EvalStepSpec extends AnyWordSpec with Matchers with MockFactory {
  "An EvalStep" when {
    val fileIOStub = stub[FileIOInterface]
    val resultTableStub = stub[ResultTableInterface]
    val controller = new Controller(RoundManager(), fileIOStub, resultTableStub)
    val evalStep = new EvalStep(controller)
    "saves the current controller's state and round manager" in {
      val state = (controller.roundManager, controller.state)
      evalStep.doStep()
      evalStep.memento should be(state)
    }

    "restore the previous state and save the current one" in {
      val oldState = (controller.roundManager.asInstanceOf[RoundManager].copy(), controller.state)
      controller.state = InGameState(controller)
      val newState = (controller.roundManager.asInstanceOf[RoundManager].copy(), controller.state)
      evalStep.undoStep()
      evalStep.memento should be(newState)
      controller.roundManager should be(oldState._1)
      controller.state should be(oldState._2)
    }

    "revert the undo command" in {
      val afterUndoState = (controller.roundManager.asInstanceOf[RoundManager].copy(), controller.state)
      val beforeUndoState = evalStep.memento
      evalStep.redoStep()
      evalStep.memento should be(afterUndoState)
      controller.roundManager should be(beforeUndoState._1)
      controller.state should be(beforeUndoState._2)
    }
  }
}
