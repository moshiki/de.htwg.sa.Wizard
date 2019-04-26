package de.htwg.se.wizard

import de.htwg.se.wizard.model.Player
import de.htwg.se.wizard.model.CardStack
import de.htwg.se.wizard.model.cards._

import scala.io.StdIn._

class TUI {
  def getNumberOfPlayers(number: Int): Int = {
    if (number < 3 || number > 5) throw new IllegalArgumentException
    number
  }

  def playerSetup(names: Array[String]): IndexedSeq[Player] = {
    for {i <- names.indices} yield Player(names(i))
  }

  def numberOfRounds(number: Int): Int = {
    number match {
      case 3 => 20
      case 4 => 15
      case 5 => 12
      case _ => throw new IllegalArgumentException
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
    val initialStack = CardStack.initialize
    val players = setup()
    val rounds = numberOfRounds(players.size)

    for {round <- 1 to rounds
      currentPlayer <- players.indices} {

      println(playerTurn(players, round, currentPlayer, initialStack))
      val cardSelection = readInt()
    }

    "See you again!"
  }

  def playerTurn(players: IndexedSeq[Player], round: Int, currentPlayer: Int, cardStack: List[Card]): String = {
    val indexGenerator = scala.util.Random
    val cards = for {i <- 1 to round} yield cardStack(indexGenerator.nextInt(cardStack.size - 1))

    val firstString = "Round " + round + " - Player " + (currentPlayer + 1) + " (" + players(currentPlayer).name + ")"
    val secondString = "Select one of the following cards:"

    firstString + "\n" + secondString + "\n" + "{ " + cards.mkString(", ") + " }"
  }
}
