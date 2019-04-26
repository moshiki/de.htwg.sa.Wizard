package de.htwg.se.wizard

import de.htwg.se.wizard.model.Player

import scala.io.StdIn._

class TUI {
  def getNumberOfPlayers(number: Int): Int = {
    if (number < 3 || number > 5) throw new IllegalArgumentException
    number
  }

  def playerSetup(names: Array[String]): IndexedSeq[Player] = {
    for {i <- 1 to names.length} yield Player(names(i))
  }

  def numberOfRounds(number: Int): Int = {
    number match {
      case 3 => 20
      case 4 => 15
      case 5 => 12
      case _ => throw IllegalArgumentException
    }
  }

  def setup(): IndexedSeq[Player] = {
    println("Welcome to Wizard!\nPlease enter the number of Players[1-6]:")
    val numberOfPlayer = getNumberOfPlayers(readInt())
    println("Please enter your names: ")
    val names:Array[String] = new Array[String](numberOfPlayer)
    for(a <- 1 to numberOfPlayer) {
      print("Player " + a + ": ")
      names.update(a-1, readLine())
    }

    playerSetup(names)
  }

  def run(): String = {
    val players = setup()
    val rounds = numberOfRounds(players.size)

    for (i <- 1 to rounds) {

    }
  }
}
