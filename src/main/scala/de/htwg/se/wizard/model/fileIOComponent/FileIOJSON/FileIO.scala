package de.htwg.se.wizard.model.fileIOComponent.FileIOJSON

import java.io.{File, PrintWriter}

import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import play.api.libs.json.Json

class FileIO extends FileIOInterface{
  override def load(modelInterface: ModelInterface): (String, ModelInterface) = ???

  override def save(controllerState: String, modelInterface: ModelInterface): Unit = {
    val state = Json.obj(
      "controllerState" -> controllerState,
      "modelState" -> modelInterface.toJson
    )

    val pw = new PrintWriter(new File("WizardSaveGame.json"))
    pw.write(Json.prettyPrint(state))
    pw.close()
  }
}
