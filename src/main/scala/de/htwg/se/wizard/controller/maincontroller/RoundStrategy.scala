package de.htwg.se.wizard.controller.maincontroller

object RoundStrategy {

  trait RoundStrategy {
    def strategy(numberOfPlayers: Int)

    def strategy3Players: RoundManager

    def strategy4Players: RoundManager

    def strategy5Players: RoundManager

    def execute(numberOfPlayers: Int, roundManager: RoundManager): RoundManager
  }

  def execute(numberOfPlayers: Int): RoundManager = strategy(numberOfPlayers)

  def strategy(numberOfPlayers: Int): RoundManager = numberOfPlayers match {
    case 3 => strategy3Players
    case 4 => strategy4Players
    case 5 => strategy5Players

  }

  def strategy3Players: RoundManager = {
    RoundManager.Builder().withNumberOfPlayers(3).withNumberOfRounds(20).build()
  }

  def strategy4Players: RoundManager = {
    RoundManager.Builder().withNumberOfPlayers(4).withNumberOfRounds(15).build()
  }

  def strategy5Players: RoundManager = {
    RoundManager.Builder().withNumberOfPlayers(5).withNumberOfRounds(12).build()
  }
}
