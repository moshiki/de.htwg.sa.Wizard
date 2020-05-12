package de.htwg.se.wizard.model.fileIOComponent

import de.htwg.se.wizard.model.modelComponent.ModelInterface

import scala.util.Try

trait FileIOInterface {
  def load(modelInterface: ModelInterface): Try[(String, ModelInterface)]

  def save(controllerState: String, modelInterface: ModelInterface): Try[Unit]
}
