package de.htwg.se.wizard.model

object RoundStrategy {
  def execute(numberOfPlayers: Int, roundManager: RoundManager): RoundManager = strategy(numberOfPlayers, roundManager)

  def strategy(numberOfPlayers: Int, roundManager: RoundManager) = numberOfPlayers match {
    case 3 => strategy3Players(roundManager)
    case 4 => strategy4Players
    case 5 => strategy5Players
    case _ => throw new IllegalArgumentException

  }

  def strategy3Players(roundManager: RoundManager) = {
    roundManager.copy(3, 20)
  }

  def strategy4Players = {
    new RoundManager(4, 15)
  }

  def strategy5Players = {
    new RoundManager(5,12)
  }
}

