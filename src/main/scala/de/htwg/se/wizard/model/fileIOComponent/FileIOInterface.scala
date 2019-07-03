package de.htwg.se.wizard.model.fileIOComponent

import de.htwg.se.wizard.model.modelComponent.ModelInterface

trait FileIOInterface {
  def load(modelInterface: ModelInterface): (String, ModelInterface)

  def save(controllerState: String, modelInterface: ModelInterface): Unit
}
