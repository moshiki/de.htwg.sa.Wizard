package de.htwg.se.wizard.model

import org.scalatest.{Matchers, WordSpec}

class ResultTableSpec extends WordSpec with Matchers{
  "A ResultTable" should {
    "initialize itself with an Array of dim 20 * 6 without any parameters" in {
      val table = ResultTable()
      table.points.length should be(20)
      table.points(0).length should be(6)
    }
    "initialize itself with an array of dim 3 * 5 when this are the parameters" in {
      val table = ResultTable(3, 5)
      table.points.length should be(3)
      table.points(0).length should be(5)
    }
    val table = ResultTable()
    "update the result correctly in the first round" in {
      table.updatePoints(1, 0, 5)
      table.points(0)(0) should be(5)
    }
    "update the result correctly in all future rounds" in {
      table.updatePoints(2, 0, -3)
      table.points(1)(0) should be(2)
    }
    "have a nice string representation" in {
      val printTable = ResultTable(2, 3)
      printTable.points(0)(1) = 5
      printTable.toString should be(
        """|###########################################
          |#      0      #      5      #      0      #
          |###########################################
          |#      0      #      0      #      0      #
          |###########################################"""
        .stripMargin)
    }
  }
}
