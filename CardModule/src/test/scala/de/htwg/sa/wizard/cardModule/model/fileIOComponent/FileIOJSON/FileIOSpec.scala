package de.htwg.sa.wizard.cardModule.model.fileIOComponent.FileIOJSON

import java.nio.file.{Files, Path}

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

import scala.io.Source
import scala.util.Success

class FileIOSpec extends AnyWordSpec with Matchers with MockFactory {
  "A JSON File IO" when {
    "saving the Card Interface" should {
      val cardInterfaceStub = stub[CardInterface]
      val expectedJson = Json.obj("CardInterfaceJson" -> "Test-Data")
      (cardInterfaceStub.toJson _).when().returns(expectedJson)
      val fileIO = FileIO()
      val path = "CardInterface"
      val result = fileIO.save(cardInterfaceStub, path)

      "ask the Card Interface for its Json data" in {
        (cardInterfaceStub.toJson _).verify()
      }

      "store the correct data in the correct path" in {
        val source = Source.fromFile(path + ".json")
        val fileContents = source.getLines.mkString
        source.close()
        val json = Json.parse(fileContents)
        json should be(expectedJson)
        Files.deleteIfExists(Path.of(path + ".json")) should be(true)
      }

      "return a Success" in {
        result.isInstanceOf[Success[Unit]] should be(true)
      }
    }

    "loading the Card Interface" should {
      "return the correct Card Interface in a success" in {
        val path = "CardModule/src/test/resources/CardInterface"
        val expectedJson = Json.obj("CardInterfaceJson" -> "Test-Data")
        val expectedCardInterface = stub[CardInterface]
        val cardInterfaceStub = stub[CardInterface]
        (cardInterfaceStub.fromJson _).when(expectedJson).returns(expectedCardInterface)
        val fileIO = FileIO()
        val result = fileIO.load(cardInterfaceStub, path)

        result.isInstanceOf[Success[CardInterface]] should be(true)
        result.get should be(expectedCardInterface)
      }
    }
  }
}
