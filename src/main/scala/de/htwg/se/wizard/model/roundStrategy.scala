package de.htwg.se.wizard.model

object RoundStrategy {
  def execute(number: Int): RoundManager = strategy(number)

  def strategy(number: Int) = number match {
    case 0 => strategy0Players
    case 3 => strategy3Players
    case 4 => strategy4Players
    case 5 => strategy5Players
    case _ => throw new IllegalArgumentException

  }

  def strategy0Players = {
    new RoundManager(0,0)
  }

  def strategy3Players = {
    new RoundManager(3, 20)
  }

  def strategy4Players = {
    new RoundManager(4, 15)
  }

  def strategy5Players = {
    new RoundManager(5,12)
  }
}

