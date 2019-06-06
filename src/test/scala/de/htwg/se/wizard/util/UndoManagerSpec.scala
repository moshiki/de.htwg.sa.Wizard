package de.htwg.se.wizard.util

import de.htwg.se.wizard.controller.{Controller, RoundManager}
import de.htwg.se.wizard.model.ResultTable
import org.scalatest.{Matchers, WordSpec}

class UndoManagerSpec extends WordSpec with Matchers{
    "An UndoManager" when {
      val resultTable = ResultTable(20,3, ResultTable.initializeVector(3, 3))
      val roundManager = RoundManager(resultTable = resultTable)
     val controller = new Controller(roundManager)

      "do undoStep" in {

      }
    }
}
