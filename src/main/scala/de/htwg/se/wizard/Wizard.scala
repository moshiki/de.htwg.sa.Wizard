package de.htwg.se.wizard

import de.htwg.se.wizard.model.Player
import scala.io.StdIn.readLine
import scala.io.StdIn.readInt

object Wizard {


  def main(args: Array[String]): Unit = {
    val tui = new TUI()
    println(tui.run())


  }
}
