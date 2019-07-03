package de.htwg.se.wizard.model.fileIOComponent.FileIOJSON

import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface

class FileIO extends FileIOInterface{
  override def load(modelInterface: ModelInterface): (String, ModelInterface) = ???

  override def save(controllerState: String, modelInterface: ModelInterface): Unit = ???
}
