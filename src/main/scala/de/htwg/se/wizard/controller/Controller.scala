package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.RoundManager
import de.htwg.se.wizard.util.Observable

class Controller(var roundManager: RoundManager) extends Observable {
  def eval(input: String): Unit = {
    if (roundManager.needsSetup && roundManager.numberOfPlayers == 0) {
      val number = Controller.toInt(input)
      if (number.isEmpty) return
      roundManager.setNumberOfPlayers(number.get)
    } else if (roundManager.needsSetup) {
      roundManager.addPlayer(input)
    } else {
      val selectedCard = Controller.toInt(input)
      if (selectedCard.isEmpty) return
      roundManager.evaluate(selectedCard.get)
    }

    notifyObservers()
  }
  def getCurrentState: String = {
    if (roundManager.needsSetup) roundManager.getSetupStrings
    else roundManager.getPlayerStateStrings
  }
}

object Controller {
  def toInt(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case _: Exception => None
    }
  }
}