package de.htwg.sa.wizard.cardModule.controller.controllerComponent

trait CardControllerInterface {
  def shuffleCardStack(): Unit
  def save(): Unit
  def load(): Unit
}
