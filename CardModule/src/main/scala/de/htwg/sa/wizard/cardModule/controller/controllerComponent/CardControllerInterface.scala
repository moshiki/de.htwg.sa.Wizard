package de.htwg.sa.wizard.cardModule.controller.controllerComponent

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface

trait CardControllerInterface {
  def shuffleCardStack(): List[CardInterface]
  def cardsForPlayer(playerNumber: Int, currentRound: Int): List[CardInterface]
  def splitCardStack(numberOfPlayers: Int, currentRound: Int): Unit
  def assignCardsForPlayer(cards: List[CardInterface], playerName: String): List[CardInterface]
  def trumpColor: Option[String]
  def save(): Unit
  def load(): Unit
  def topOfCardStackString(): String
  def playerOfHighestCard(list: List[CardInterface]): String
}
