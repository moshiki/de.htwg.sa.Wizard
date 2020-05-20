package de.htwg.sa.wizard.model.cardComponent.fileIOComponent.FileIOXML

import java.nio.file.{Files, Path}

import de.htwg.sa.wizard.model.cardComponent.CardInterface
import de.htwg.sa.wizard.model.fileIOComponent.FileIOXML.FileIO
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.xml.{Elem, XML}

class FileIOSpec extends AnyWordSpec with Matchers with MockFactory {
  "An XML FileIO" when {
    "saving the Card Interface" should {
      val cardInterfaceStub = stub[CardInterface]
      val expectedXML: Elem = <CardInterface>testData</CardInterface>
      (cardInterfaceStub.toXML _).when().returns(expectedXML)
      val fileIO = FileIO()
      val path = "CardInterface"
      val result = fileIO.save(cardInterfaceStub, path)

      "ask the cardInterface for its data in XML" in {
        (cardInterfaceStub.toXML _).verify()
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

    "loading the Card Interface" should {
      "return the correct Card Interface in a success" in {
        val path = "CardModule/src/test/resources/CardInterface"
        val expectedXML: Elem = <CardInterface>testData</CardInterface>
        val expectedCardInterface = stub[CardInterface]
        val cardInterfaceStub = stub[CardInterface]
        (cardInterfaceStub.fromXML _).when(expectedXML).returns(expectedCardInterface)
        val fileIO = FileIO()
        val result = fileIO.load(cardInterfaceStub, path)

        result.isSuccess should be(true)
        result.get should be(expectedCardInterface)
      }
    }
  }
}
