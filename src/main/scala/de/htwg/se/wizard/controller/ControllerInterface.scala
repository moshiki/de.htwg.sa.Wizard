package de.htwg.se.wizard.controller

import de.htwg.se.wizard.util.Observable

trait ControllerInterface extends Observable{
  def undo(): Unit
  def redo(): Unit
  def eval(input: String): Unit

  def getCurrentStateAsString: String
}
