package de.htwg.sa.wizard.cardModule.model.fileIOComponent.FileIOJSON

import java.io.{File, PrintWriter}

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface
import de.htwg.sa.wizard.cardModule.model.fileIOComponent.FileIOInterface
import play.api.libs.json.Json

import scala.io.Source
import scala.util.Try

case class FileIO() extends FileIOInterface {
  override def load(cardInterface: CardInterface, path: String): Try[CardInterface] = {
    Try{
      val source = Source.fromFile(path + ".json")
      val string = source.getLines().mkString
      source.close
      val json = Json.parse(string)
      cardInterface.fromJson(json)
    }
  }

  override def save(cardInterface: CardInterface, path: String): Try[Unit] = {
    Try{
      val printWriter = new PrintWriter(new File(path + ".json"))
      printWriter.write(Json.prettyPrint(cardInterface.toJson))
      printWriter.close()
    }
  }
}
