package de.htwg.se.wizard

import de.htwg.se.wizard.model.Player

class TUI {
  def getNumberOfPlayers(number: Int): Int = {
    if (number < 3 || number > 6) throw new IllegalArgumentException
    number
  }

  def playerSetup(names: Array[String]): IndexedSeq[Player] = {
    for {i <- 1 to names.length} yield Player(names(i))
  }
}
