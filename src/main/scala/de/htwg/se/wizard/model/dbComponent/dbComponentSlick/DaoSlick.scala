package de.htwg.se.wizard.model.dbComponent.dbComponentSlick

import de.htwg.se.wizard.model.dbComponent.DaoInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface

case class DaoSlick() extends DaoInterface {
  override def load(modelInterface: ModelInterface): (ModelInterface, String) = ???

  override def save(modelInterface: ModelInterface, controllerState: String): Unit = ???
}
