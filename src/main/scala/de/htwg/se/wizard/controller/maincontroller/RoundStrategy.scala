package de.htwg.se.wizard.controller.maincontroller

import de.htwg.se.wizard.model.PlayerInterface

object RoundStrategy {

  abstract class RoundStrategy(playerInterface: PlayerInterface) {
    def strategy(numberOfPlayers: Int, playerInterface: PlayerInterface)

    def strategy3Players(playerInterface: PlayerInterface): RoundManager

    def strategy4Players(playerInterface: PlayerInterface): RoundManager

    def strategy5Players(playerInterface: PlayerInterface): RoundManager

    def execute(numberOfPlayers: Int, roundManager: RoundManager): RoundManager
  }

  def execute(numberOfPlayers: Int, playerInterface: PlayerInterface): RoundManager = strategy(numberOfPlayers, playerInterface)

  def strategy(numberOfPlayers: Int, playerInterface: PlayerInterface): RoundManager = numberOfPlayers match {
    case 3 => strategy3Players(playerInterface)
    case 4 => strategy4Players(playerInterface)
    case 5 => strategy5Players(playerInterface)

  }

  def strategy3Players(playerInterface: PlayerInterface): RoundManager = {
    RoundManager.Builder().withNumberOfPlayers(3).withNumberOfRounds(20).build(playerInterface)
  }

  def strategy4Players(playerInterface: PlayerInterface): RoundManager = {
    RoundManager.Builder().withNumberOfPlayers(4).withNumberOfRounds(15).build(playerInterface)
  }

  def strategy5Players(playerInterface: PlayerInterface): RoundManager = {
    RoundManager.Builder().withNumberOfPlayers(5).withNumberOfRounds(12).build(playerInterface)
  }
}
