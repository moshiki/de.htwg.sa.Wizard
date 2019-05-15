package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.RoundManager
import de.htwg.se.wizard.util.Observable

class Controller(var roundManager: RoundManager) extends Observable {
  var state: ControllerState = preSetupState(roundManager) //preSetup, setup, game, gameOver

  def nextState(): Unit = state = state.nextState


  def eval(input: String): Unit = {
    state.eval(input)
    notifyObservers()
  }

  def getCurrentStateAsString: String = state.getCurrentStateAsString
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


trait ControllerState {
  def eval(input: String): Unit

  def getCurrentStateAsString: String

  def nextState: ControllerState
}


case class preSetupState(roundManager: RoundManager) extends ControllerState {
  override def eval(input: String): Unit = {
    val number = Controller.toInt(input)
    if (number.isEmpty) return
    roundManager.setNumberOfPlayers(number.get)
  }

  override def getCurrentStateAsString: String = "Welcome to Wizard!\nPlease enter the number of Players[3-5]:"

  override def nextState: ControllerState = setupState(roundManager)
}


case class setupState(roundManager: RoundManager) extends ControllerState {
  override def eval(input: String): Unit = roundManager.addPlayer(input)

  override def getCurrentStateAsString: String = roundManager.getSetupStrings

  override def nextState: ControllerState = inGameState(roundManager)
}


case class inGameState(roundManager: RoundManager) extends ControllerState {
  override def eval(input: String): Unit = {
    val selectedCard = Controller.toInt(input)
    if (selectedCard.isEmpty) return
    roundManager.evaluate(selectedCard.get)
  }

  override def getCurrentStateAsString: String = roundManager.getPlayerStateStrings

  override def nextState: ControllerState = gameOverState(roundManager)
}


case class gameOverState(roundManager: RoundManager) extends ControllerState {
  override def eval(input: String): Unit = ()

  override def getCurrentStateAsString: String = "\nGame Over! Press 'q' to quit."

  override def nextState: ControllerState = this
}