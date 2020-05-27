package de.htwg.sa.wizard.cardModule.controller.controllerComponent

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface

trait CardControllerInterface {
  def shuffleCardStack(): List[CardInterface]
  def cardsForPlayer(playerNumber: Int, currentRound: Int): List[CardInterface]
  def trumpColor: Option[String]
  def save(): Unit
  def load(): Unit
}
