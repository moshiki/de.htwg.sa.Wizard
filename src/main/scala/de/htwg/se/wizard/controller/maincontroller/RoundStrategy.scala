package de.htwg.se.wizard.controller.maincontroller

import de.htwg.se.wizard.model.{CardInterface, PlayerInterface, StaticResultTableInterface}

object RoundStrategy {

  abstract class RoundStrategy(playerInterface: PlayerInterface) {
    def strategy(numberOfPlayers: Int, playerInterface: PlayerInterface)

    def strategy3Players(playerInterface: PlayerInterface, cardInterface: CardInterface): RoundManager

    def strategy4Players(playerInterface: PlayerInterface, cardInterface: CardInterface): RoundManager

    def strategy5Players(playerInterface: PlayerInterface, cardInterface: CardInterface): RoundManager

    def execute(numberOfPlayers: Int, roundManager: RoundManager): RoundManager
  }

  def execute(numberOfPlayers: Int, playerInterface: PlayerInterface,
              cardInterface: CardInterface, staticResultTableInterface: StaticResultTableInterface): RoundManager =
    strategy(numberOfPlayers, playerInterface, cardInterface, staticResultTableInterface)

  def strategy(numberOfPlayers: Int, playerInterface: PlayerInterface,
               cardInterface: CardInterface, staticResultTableInterface: StaticResultTableInterface): RoundManager = numberOfPlayers match {
    case 3 => strategy3Players(playerInterface, cardInterface, staticResultTableInterface)
    case 4 => strategy4Players(playerInterface, cardInterface, staticResultTableInterface)
    case 5 => strategy5Players(playerInterface, cardInterface, staticResultTableInterface)

  }

  def strategy3Players(playerInterface: PlayerInterface, cardInterface: CardInterface, resultTableInterface: StaticResultTableInterface): RoundManager = {
    RoundManager.Builder()
      .withNumberOfPlayers(3)
      .withNumberOfRounds(20)
      .build(playerInterface, cardInterface, resultTableInterface)
  }

  def strategy4Players(playerInterface: PlayerInterface, cardInterface: CardInterface, resultTableInterface: StaticResultTableInterface): RoundManager = {
    RoundManager.Builder().
      withNumberOfPlayers(4).
      withNumberOfRounds(15).
      build(playerInterface, cardInterface, resultTableInterface)
  }

  def strategy5Players(playerInterface: PlayerInterface, cardInterface: CardInterface, resultTableInterface: StaticResultTableInterface): RoundManager = {
    RoundManager.Builder()
      .withNumberOfPlayers(5)
      .withNumberOfRounds(12)
      .build(playerInterface, cardInterface, resultTableInterface)
  }
}
