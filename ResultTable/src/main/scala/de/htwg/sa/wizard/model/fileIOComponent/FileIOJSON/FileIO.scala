package de.htwg.sa.wizard.model.fileIOComponent.FileIOJSON

import java.io.{File, PrintWriter}

import de.htwg.sa.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.sa.wizard.model.resultTableComponent.ResultTableInterface
import play.api.libs.json.Json

import scala.io.Source
import scala.util.Try

case class FileIO() extends FileIOInterface {
  override def load(resultTableInterface: ResultTableInterface, path: String): Try[ResultTableInterface] = {
    Try{
      val source = Source.fromFile(path + ".json")
      val string = source.getLines().mkString
      source.close
      val json = Json.parse(string)
      resultTableInterface.fromJson(json)
    }
  }

  override def save(resultTableInterface: ResultTableInterface, path: String): Try[Unit] = {
    Try{
      val printWriter = new PrintWriter(new File(path + ".json"))
      printWriter.write(Json.prettyPrint(resultTableInterface.toJson))
      printWriter.close()
    }
  }
}
