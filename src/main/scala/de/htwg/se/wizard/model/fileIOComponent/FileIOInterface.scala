package de.htwg.se.wizard.model.fileIOComponent

import de.htwg.se.wizard.model.modelComponent.RoundManager

trait FileIOInterface {
  def load(roundManager: RoundManager): (String, RoundManager)
  def save (controllerState: String, roundManager: RoundManager): Unit
}
