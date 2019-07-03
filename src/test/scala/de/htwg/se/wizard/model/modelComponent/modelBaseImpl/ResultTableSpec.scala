package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class ResultTableSpec extends WordSpec with Matchers {
  "A ResultTable" should {
    val table = ResultTable.initializeTable(20, 3)
    "set ResultTable correctly" in {
      table.numberOfPlayers should be(3)
      table.roundsToPlay should be(20)
    }

    "update the result correctly in the first round" in {

      val newTable = table.updatePoints(1, 0, 10)
      newTable.points(0)(0) should be(10)
    }

    "update the result correctly in all future rounds" in {
      val newTable = table.updatePoints(2, 0, -20)
      newTable.points(1)(0) should be(-20)
    }

    "have a nice string representation" in {
      val printTable = ResultTable.initializeTable(2, 3)
      val newPrintTable = printTable.updatePoints(1, 1, 5)
      newPrintTable.toString should be(
        """|#  Player  1  #  Player  2  #  Player  3  #
           |###########################################
           |#      0      #      5      #      0      #
           |###########################################
           |#      0      #      0      #      0      #
           |###########################################"""
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
      val table2 = json.validate[ResultTable].asOpt.get
      table2 should be(table)
    }
  }
}
