package de.htwg.sa.wizard.resultTable.controller.controllerComponent.controllerBaseImplementation

import de.htwg.sa.wizard.resultTable.model.fileIOComponent.FileIOInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.resultTableBaseImplementation.ResultTable
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.{Failure, Success}

class ResultTableControllerSpec extends AnyWordSpec with Matchers with MockFactory {
  "A ResultTableController" should {
    "be able to update the points stored in the result table correctly" in {
      val resultTableMock = mock[ResultTableInterface]
      val fileIOStub = stub[FileIOInterface]
      val controller = ResultTableController(resultTableMock, fileIOStub)
      val points = Vector(7, 8, 9)
      inSequence(
        (resultTableMock.updatePoints _).expects(5, 0, 7).returning(resultTableMock),
        (resultTableMock.updatePoints _).expects(5, 1, 8).returning(resultTableMock),
        (resultTableMock.updatePoints _).expects(5, 2, 9).returning(resultTableMock),
      )

      controller.updatePoints(5, points)
    }

    "initialize the result table correctly" in {
      val resultTableMock = mock[ResultTableInterface]
      val fileIOStub = stub[FileIOInterface]
      val controller = ResultTableController(resultTableMock, fileIOStub)
      val numberOfRounds = 1
      val numberOfPlayers = 2
      (resultTableMock.initializeTable _).expects(numberOfRounds, numberOfPlayers)

      controller.initializeTable(numberOfRounds, numberOfPlayers)
    }

    "invoke saving the result table data correctly" in {
      val fileName = "ResultTableModule"
      val resultTableStub = stub[ResultTableInterface]
      val fileIOMock = mock[FileIOInterface]
      val controller = ResultTableController(resultTableStub, fileIOMock)
      (fileIOMock.save _).expects(resultTableStub, fileName)

      controller.save()
    }

    "store the stored result data in case of a success" in {
      val fileName = "ResultTableModule"
      val resultTableStub = stub[ResultTableInterface]
      val expectedResultTableStub = stub[ResultTableInterface]
      val fileIOMock = mock[FileIOInterface]
      val controller = ResultTableController(resultTableStub, fileIOMock)
      (fileIOMock.load _).expects(resultTableStub, fileName).returning(Success(expectedResultTableStub))

      controller.load()
      controller.resultTableInterface should be(expectedResultTableStub)
    }

    "not store the stored result data in case of a failure" in {
      val fileName = "ResultTableModule"
      val resultTableStub = stub[ResultTableInterface]
      val fileIOMock = mock[FileIOInterface]
      val controller = ResultTableController(resultTableStub, fileIOMock)
      (fileIOMock.load _).expects(resultTableStub, fileName).returning(Failure(new Exception))

      controller.load()
      controller.resultTableInterface should be(resultTableStub)
    }

    "return the same array of objects as returned by the result table" in {
      val resultTableMock = mock[ResultTableInterface]
      val fileIOStub = stub[FileIOInterface]
      val controller = ResultTableController(resultTableMock, fileIOStub)
      val expectedArray = Array(Array(5))
      (resultTableMock.toArray _).expects().returning(expectedArray)

      controller.pointArrayForView should be(expectedArray)
    }

    "return the string representation of the result table" in {
      // Sadly ScalaMock does not support mocking toString methods, therefore no mocking
      val resultTable = ResultTable().initializeTable(5, 3)
      val fileIOStub = stub[FileIOInterface]
      val controller = ResultTableController(resultTable, fileIOStub)

      controller.tableAsString should be(resultTable.toString)
    }

    "return the list of players stored in the result table" in {
      val resultTableMock = mock[ResultTableInterface]
      val fileIOStub = stub[FileIOInterface]
      val controller = ResultTableController(resultTableMock, fileIOStub)
      val expectedPlayerList = List("P1", "P2")
      (resultTableMock.playerNames _).expects().returning(expectedPlayerList)

      controller.playerList should be(expectedPlayerList)
    }
  }
}
