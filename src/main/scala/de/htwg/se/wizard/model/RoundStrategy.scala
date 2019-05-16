package de.htwg.se.wizard.model

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
    roundManager.copy(3, 20)
  }

  def strategy4Players(roundManager: RoundManager):RoundManager = {
    roundManager.copy(4, 15)
  }

  def strategy5Players(roundManager: RoundManager):RoundManager = {
     roundManager.copy(5,12)
  }
}

