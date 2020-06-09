package de.htwg.se.wizard.model.dbComponent

import de.htwg.se.wizard.model.modelComponent.ModelInterface

trait DaoInterface {
  def load(modelInterface: ModelInterface): (ModelInterface, String)

  def save(modelInterface: ModelInterface, controllerState: String): Unit
}
