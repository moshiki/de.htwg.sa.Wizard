package de.htwg.se.wizard.util

trait Command {

  def doStep():Unit
  def undoStep():Unit
  def redoStep():Unit

}

