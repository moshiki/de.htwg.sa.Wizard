package de.htwg.se.wizard

import de.htwg.se.wizard.model.Player
import scala.io.StdIn.readLine
import scala.io.StdIn.readInt

object Wizard {
  def getNumberOfPlayers(number: Int): Int = {
    if (number < 3 || number > 6) throw new IllegalArgumentException
    number
  }

  def main(args: Array[String]): Unit = {
    val student = Player("Vanessa and Flo")
    println("Welcome to Wizard, a project for SE by " + student)

    print("Give the number of Players: ")
    val numberOfPlayers = getNumberOfPlayers(readInt())
    println("Number of Players: " + numberOfPlayers)

    println("Please enter your names: ")
    val names:Array[String] = new Array[String](numberOfPlayers)
    //val names = Seq()

    for(a <- 1 to numberOfPlayers) {
      print("Player " + a + " ")
      val player = Player(readLine())
      names.update(a-1, player.toString)
    }
    println(names.mkString(", "))

    var input = ""

    while(input != "q") {
      input = readLine()
    }
    if(input == "q") {
      System.exit(1)
    }



  }
}
