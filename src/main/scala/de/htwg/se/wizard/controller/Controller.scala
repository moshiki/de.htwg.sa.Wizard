package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.RoundManager
import de.htwg.se.wizard.util.Observable

class Controller(var roundManager: RoundManager) extends Observable {
  var state: ControllerState = preSetupState(roundManager) //preSetup, setup, game, gameOver

  def nextState(): Unit = state = state.nextState


  def eval(input: String): Unit = {
    if (roundManager.needsSetup && roundManager.numberOfPlayers == 0) {

    } else if (roundManager.needsSetup) {

    } else {
      val selectedCard = Controller.toInt(input)
      if (selectedCard.isEmpty) return
      roundManager.evaluate(selectedCard.get)
    }

    notifyObservers()
  }

  def getCurrentState: String = {
    if (roundManager.needsSetup)
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


trait ControllerState {
  def eval(input: String): Unit

  def getCurrentState: String

  def nextState: ControllerState
}


case class preSetupState(roundManager: RoundManager) extends ControllerState {
  override def eval(input: String): Unit = {
    val number = Controller.toInt(input)
    if (number.isEmpty) return
    roundManager.setNumberOfPlayers(number.get)
  }

  override def getCurrentState: String = "Welcome to Wizard!\nPlease enter the number of Players[3-5]:"

  override def nextState: ControllerState = setupState(roundManager)
}


case class setupState(roundManager: RoundManager) extends ControllerState {
  override def eval(input: String): Unit = roundManager.addPlayer(input)

  override def getCurrentState: String = roundManager.getSetupStrings

  override def nextState: ControllerState = inGameState(roundManager)
}


case class inGameState(roundManager: RoundManager) extends ControllerState {
  override def eval(input: String): Unit = ???

  override def getCurrentState: String = ???

  override def nextState: ControllerState = ???
}


case class gameOverState(roundManager: RoundManager) extends ControllerState {
  override def eval(input: String): Unit = ???

  override def getCurrentState: String = ???

  override def nextState: ControllerState = ???
}