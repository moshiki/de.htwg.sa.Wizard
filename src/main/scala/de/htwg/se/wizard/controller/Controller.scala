package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.Player
import de.htwg.se.wizard.util.Observable

class Controller(var players: IndexedSeq[Player]) extends Observable {
  def getCurrentPlayerState: String =
}
