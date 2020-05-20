package de.htwg.sa.wizard.resultTable.model.fileIOComponent.FileIOXML

import java.nio.file.{Files, Path}
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.xml.{Elem, XML}

class FileIOSpec extends AnyWordSpec with Matchers with MockFactory {
  "An XML FileIO" when {
    "saving the result table" should {
      val resultTableStub = stub[ResultTableInterface]
      val expectedXML: Elem = <ResultTable>testData</ResultTable>
      (resultTableStub.toXML _).when().returns(expectedXML)
      val fileIO = FileIO()
      val path = "ResultTable.xml"
      val result = fileIO.save(resultTableStub, path)

      "ask the resultTable for its data in XML" in {
        (resultTableStub.toXML _).verify()
      }

      "store the correct data in the correct file" in {
        val storedXML = XML.loadFile(path + ".xml")
        storedXML should be(expectedXML)
        Files.deleteIfExists(Path.of(path + ".xml")) should be(true)
      }

      "return a Success" in {
        result.isSuccess should be(true)
      }
    }

    "loading the result table" should {
      "return the correct result table in a success" in {
        val path = "ResultTable/src/test/resources/ResultTable"
        val expectedXML: Elem = <ResultTable>testData</ResultTable>
        val expectedResultTable = stub[ResultTableInterface]
        val resultTableStub = stub[ResultTableInterface]
        (resultTableStub.fromXML _).when(expectedXML).returns(expectedResultTable)
        val fileIO = FileIO()
        val result = fileIO.load(resultTableStub, path)

        result.isSuccess should be(true)
        result.get should be(expectedResultTable)
      }
    }
  }
}
