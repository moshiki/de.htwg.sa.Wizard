package de.htwg.sa.wizard.model.fileIOComponent.FileIOJSON

import java.nio.file.{Files, Path}

import de.htwg.sa.wizard.model.resultTableComponent.ResultTableInterface
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

import scala.io.Source
import scala.util.Success

class FileIOSpec extends AnyWordSpec with Matchers with MockFactory {
  "A JSON File IO" when {
    "saving the result table" should {
      val resultTableStub = stub[ResultTableInterface]
      val expectedJson = Json.obj("resultTableJson" -> "Test-Data")
      (resultTableStub.toJson _).when().returns(expectedJson)
      val fileIO = FileIO()
      val path = "ResultTable"
      val result = fileIO.save(resultTableStub, path)

      "ask the resultTable for its Json data" in {
        (resultTableStub.toJson _).verify()
      }

      "store the correct data in the correct path" in {
        val source = Source.fromFile(path)
        val fileContents = source.getLines.mkString
        source.close()
        val json = Json.parse(fileContents)
        json should be(expectedJson)
        Files.deleteIfExists(Path.of(path)) should be(true)
      }

      "return a Success" in {
        result.isInstanceOf[Success[Unit]] should be(true)
      }
    }

    "loading the result table" should {
      "return the correct result table in a success" in {
        val path = "ResultTable/src/test/resources/ResultTable"
        val expectedJson = Json.obj("resultTableJson" -> "Test-Data")
        val expectedResultTable = stub[ResultTableInterface]
        val resultTableStub = stub[ResultTableInterface]
        (resultTableStub.fromJson _).when(expectedJson).returns(expectedResultTable)
        val fileIO = FileIO()
        val result = fileIO.load(resultTableStub, path)

        result.isInstanceOf[Success[ResultTableInterface]] should be(true)
        result.get should be(expectedResultTable)
      }
    }
  }
}
