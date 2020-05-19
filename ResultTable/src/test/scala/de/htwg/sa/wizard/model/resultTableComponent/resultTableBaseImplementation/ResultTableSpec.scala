package de.htwg.sa.wizard.model.resultTableComponent.resultTableBaseImplementation

import de.htwg.sa.wizard.model.resultTableComponent.resultTableBaseImplementation
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class ResultTableSpec extends AnyWordSpec with Matchers {
  "A ResultTable" should {
    val table = ResultTable.initializeTable(20, 3)
    "set ResultTable correctly" in {
      table.numberOfPlayers should be(3)
      table.roundsToPlay should be(20)
    }

    "update the result correctly in the first round" in {

      val newTable = table.updatePoints(1, 0, 10)
      newTable.asInstanceOf[resultTableBaseImplementation.ResultTable].points(0)(0) should be(10)
    }

    "update the result correctly in all future rounds" in {
      val newTable = table.updatePoints(2, 0, -20)
      newTable.asInstanceOf[resultTableBaseImplementation.ResultTable].points(1)(0) should be(-20)
    }

    "have a nice string representation" in {
      val printTable = ResultTable.initializeTable(2, 3)
      val newPrintTable = printTable.updatePoints(1, 1, 5)
      newPrintTable.toString should be(
        """┌──────────────────────────┬─────────────────────────┬─────────────────────────┐
          |│Player 1                  │Player 2                 │Player 3                 │
          |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
          |│0                         │5                        │0                        │
          |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
          |│0                         │0                        │0                        │
          |└──────────────────────────┴─────────────────────────┴─────────────────────────┘"""
          .stripMargin)
    }

    "convert its data into an AnyArray" in {
      val arr = table.toAnyArray
      for (i <- arr.indices) {
        for (j <- arr(i).indices) {
          arr(i)(j) should equal(table.points(i)(j))
        }
      }
    }

    "be able to store itself in an xml representation and restore successfully" in {
      val xml = table.toXML
      val newTable = table.fromXML(xml)
      newTable should be(table)
    }

    "be able to store itself in a json representation and restore successfully" in {
      val json = Json.toJson(table)
      val restoredTable = json.validate[ResultTable].asOpt.get
      restoredTable should be(table)
      table.toJson should be(json)
      table.fromJson(json) should be(restoredTable)
    }
  }
}
