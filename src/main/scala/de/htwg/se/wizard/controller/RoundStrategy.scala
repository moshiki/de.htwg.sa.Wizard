package de.htwg.se.wizard.controller

object RoundStrategy {
  trait RoundStrategy {
    def strategy(numberOfPlayers: Int, roundManager: RoundManager)
    def strategy3Players(roundManager: RoundManager):RoundManager
    def strategy4Players(roundManager: RoundManager):RoundManager
    def strategy5Players(roundManager: RoundManager):RoundManager
    def execute(numberOfPlayers: Int, roundManager: RoundManager): RoundManager
  }

  def execute(numberOfPlayers: Int, roundManager: RoundManager): RoundManager = strategy(numberOfPlayers, roundManager)

  def strategy(numberOfPlayers: Int, roundManager: RoundManager):RoundManager = numberOfPlayers match {
    case 3 => strategy3Players(roundManager)
    case 4 => strategy4Players(roundManager)
    case 5 => strategy5Players(roundManager)

  }

  def strategy3Players(roundManager: RoundManager):RoundManager = {
    RoundManager.Builder().withNumberOfPlayers(3).withNumberOfRounds(20).build()
  }

  def strategy4Players(roundManager: RoundManager):RoundManager = {
    RoundManager.Builder().withNumberOfPlayers(4).withNumberOfRounds(15).build()
  }

  def strategy5Players(roundManager: RoundManager):RoundManager = {
    RoundManager.Builder().withNumberOfPlayers(5).withNumberOfRounds(12).build()
  }
}
