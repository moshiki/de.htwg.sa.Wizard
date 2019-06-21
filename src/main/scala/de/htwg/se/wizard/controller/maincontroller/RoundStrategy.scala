package de.htwg.se.wizard.controller.maincontroller

import de.htwg.se.wizard.model.{ResultTableBuilderInterface, StaticCardInterface, StaticPlayerInterface}

object RoundStrategy {

  abstract class RoundStrategy(playerInterface: StaticPlayerInterface) {
    def strategy(numberOfPlayers: Int, playerInterface: StaticPlayerInterface)

    def strategy3Players(playerInterface: StaticPlayerInterface, cardInterface: StaticCardInterface): RoundManager

    def strategy4Players(playerInterface: StaticPlayerInterface, cardInterface: StaticCardInterface): RoundManager

    def strategy5Players(playerInterface: StaticPlayerInterface, cardInterface: StaticCardInterface): RoundManager

    def execute(numberOfPlayers: Int, roundManager: RoundManager): RoundManager
  }

  def execute(numberOfPlayers: Int, playerInterface: StaticPlayerInterface,
              cardInterface: StaticCardInterface, staticResultTableInterface: ResultTableBuilderInterface): RoundManager =
    strategy(numberOfPlayers, playerInterface, cardInterface, staticResultTableInterface)

  def strategy(numberOfPlayers: Int, playerInterface: StaticPlayerInterface,
               cardInterface: StaticCardInterface, staticResultTableInterface: ResultTableBuilderInterface): RoundManager = numberOfPlayers match {
    case 3 => strategy3Players(playerInterface, cardInterface, staticResultTableInterface)
    case 4 => strategy4Players(playerInterface, cardInterface, staticResultTableInterface)
    case 5 => strategy5Players(playerInterface, cardInterface, staticResultTableInterface)

  }

  def strategy3Players(playerInterface: StaticPlayerInterface, cardInterface: StaticCardInterface, resultTableInterface: ResultTableBuilderInterface): RoundManager = {
    RoundManager.Builder()
      .withNumberOfPlayers(3)
      .withNumberOfRounds(20)
      .build(playerInterface, cardInterface, resultTableInterface)
  }

  def strategy4Players(playerInterface: StaticPlayerInterface, cardInterface: StaticCardInterface, resultTableInterface: ResultTableBuilderInterface): RoundManager = {
    RoundManager.Builder().
      withNumberOfPlayers(4).
      withNumberOfRounds(15).
      build(playerInterface, cardInterface, resultTableInterface)
  }

  def strategy5Players(playerInterface: StaticPlayerInterface, cardInterface: StaticCardInterface, resultTableInterface: ResultTableBuilderInterface): RoundManager = {
    RoundManager.Builder()
      .withNumberOfPlayers(5)
      .withNumberOfRounds(12)
      .build(playerInterface, cardInterface, resultTableInterface)
  }
}
