package de.htwg.se.wizard.aview

import de.htwg.se.wizard.controller.Controller
import de.htwg.se.wizard.model.{CardStack, Player}
import de.htwg.se.wizard.model.cards._
import de.htwg.se.wizard.util.Observer

import scala.io.StdIn._

class TUI(controller: Controller) extends Observer{

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

  override def update(): Unit = println(controller.getCurrentPlayerState)
}
