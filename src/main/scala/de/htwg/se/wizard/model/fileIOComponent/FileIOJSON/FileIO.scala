package de.htwg.se.wizard.model.fileIOComponent.FileIOJSON

import java.io.{File, PrintWriter}

import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import play.api.libs.json.Json

import scala.io.Source
import scala.util.Try

case class FileIO() extends FileIOInterface{
  override def load(modelInterface: ModelInterface): Try[(String, ModelInterface)] = {
    Try {
      val source = Source.fromFile("WizardSaveGame.json")
      val string = source.getLines.mkString
      source.close()
      val json = Json.parse(string)
      val controllerState = (json \ "controllerState").get.as[String]
      val model = modelInterface.fromJson((json \ "modelState").get)
      (controllerState, model)
    }
  }

  override def save(controllerState: String, modelInterface: ModelInterface): Try[Unit] = {
    val state = Json.obj(
      "controllerState" -> controllerState,
      "modelState" -> modelInterface.toJson
    )

    Try {
      val pw = new PrintWriter(new File("WizardSaveGame.json"))
      pw.write(Json.prettyPrint(state))
      pw.close()
    }
  }
}
