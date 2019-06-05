package de.htwg.se.wizard.model

import org.scalatest.{Matchers, WordSpec}

class ResultTableSpec extends WordSpec with Matchers{
  "A ResultTable" should {

    "set ResultTable correctly" in {
      val table = ResultTable(20, 3, ResultTable.initializeVector(20,3))
      table.numberOfPlayers should be(3)
      table.roundsToPlay should be(20)
    }

    val table = ResultTable(20,3, ResultTable.initializeVector(3,3))
    "update the result correctly in the first round" in {

     val newTable = table.updatePoints(1, 0, 10)
      newTable.points(0)(0)should be(10)
    }

    //callculate new value?
    "update the result correctly in all future rounds" in {
      val newTable = table.updatePoints(2, 0, -20)
      newTable.points(1)(0) should be(-20)
    }

    "have a nice string representation" in {
      val printTable = ResultTable(2,3,ResultTable.initializeVector(20,3))
      val newPrintTable = printTable.updatePoints(1,1,5)
      newPrintTable.toString should be(
        """|#  Player  1  #  Player  2  #  Player  3  #
           |###########################################
           |#      0      #      5      #      0      #
           |###########################################
           |#      0      #      0      #      0      #
           |###########################################"""
        .stripMargin)
    }
  }
}
