package de.htwg.se.wizard.model.dbComponent

import de.htwg.se.wizard.model.modelComponent.ModelInterface

import scala.concurrent.Future

trait DaoInterface {
  def load(modelInterface: ModelInterface): Future[(ModelInterface, String)]

  def save(modelInterface: ModelInterface, controllerState: String): Future[Unit]
}
