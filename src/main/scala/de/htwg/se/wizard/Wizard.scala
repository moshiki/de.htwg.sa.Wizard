package de.htwg.se.wizard

import de.htwg.se.wizard.model.Player
import scala.io.StdIn.readLine

object Wizard {
  def main(args: Array[String]): Unit = {
    val student = Player("Vanessa and Flo")
    println("Welcome to Wizard, a project for SE by " + student)

    print("Give the number of Players: ")
    val numberOfPlayers = readLine()
    println("Number of Players: " + numberOfPlayers)


  }
}
