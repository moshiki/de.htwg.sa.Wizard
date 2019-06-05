package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.ResultTable
import org.scalatest.{Matchers, WordSpec}

class EvalStepSpec extends WordSpec with Matchers {
  "An EvalStep" when {
    val resultTable = ResultTable(20, 3, ResultTable.initializeVector(3, 3))
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager)
    var memento: (RoundManager, ControllerState) = (controller.roundManager.copy(), controller.state)

    "do undoStep" in {
      val newMemento = (controller.roundManager.copy(), controller.state)
      controller.roundManager should be(memento._1)
      controller.state should be(memento._2)
      memento should be(newMemento)
    }
  }

}
