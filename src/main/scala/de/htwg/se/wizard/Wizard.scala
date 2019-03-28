package de.htwg.se.wizard

import de.htwg.se.wizard.model.Player

object Wizard {
  def main(args: Array[String]): Unit = {
    val student = Player("Vanessa and Flo")
    println("Welcome to Wizard, a project for SE by " + student)
  }
}
