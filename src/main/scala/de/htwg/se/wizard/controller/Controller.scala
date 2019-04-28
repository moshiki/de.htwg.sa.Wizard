package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.{Player, RoundManager}
import de.htwg.se.wizard.util.Observable

class Controller(var roundManager: RoundManager) extends Observable {
  def eval(input: String): Unit = roundManager.eval(input)
  def getCurrentState: String = roundManager.currentStateToString
}
