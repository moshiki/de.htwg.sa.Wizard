package de.htwg.se.wizard.controller

import de.htwg.se.wizard.util.{Observable, ControllerUpdateStateObserver}

class Controller(var roundManager: RoundManager) extends Observable with ControllerUpdateStateObserver {
  roundManager.add(this)

  var state: ControllerState = preSetupState(roundManager, this)

  def eval(input: String): Unit = {
   /* if(inGameState == 0) state.evalPlayerPrediction(input)*/
    state.eval(input)
    notifyObservers()
  }

  def getCurrentStateAsString: String = state.getCurrentStateAsString

  def nextState(): Unit = state = state.nextState; roundManager = state.getRoundManager

  override def switchToNextState(): Unit = nextState()
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


abstract class ControllerState(roundManager: RoundManager) {
  def eval(input: String): Unit

 /* def evalPlayerPrediction(input: String): Unit*/

  def getCurrentStateAsString: String

  def nextState: ControllerState

  def getRoundManager: RoundManager = roundManager
}


case class preSetupState(var roundManager: RoundManager, controller: Controller) extends ControllerState(roundManager) {
  override def eval(input: String): Unit = {
    val number = Controller.toInt(input)
    if (number.isEmpty) return
    if (!roundManager.checkNumberOfPlayers(number.get)) return
    roundManager = RoundStrategy.execute(number.get, roundManager)
    roundManager.add(controller)
    controller.switchToNextState()
  }

  /*override def evalPlayerPrediction(input: String): Unit = ()*/

  override def getCurrentStateAsString: String = "Welcome to Wizard!\nPlease enter the number of Players[3-5]:"

  override def nextState: ControllerState = setupState(roundManager)
}


case class setupState(roundManager: RoundManager) extends ControllerState(roundManager) {
  override def eval(input: String): Unit = roundManager.addPlayer(input)

  /*override def evalPlayerPrediction(input: String): Unit = ()*/

  override def getCurrentStateAsString: String = roundManager.getSetupStrings

  override def nextState: ControllerState = inGameState(roundManager)
}


case class inGameState(roundManager: RoundManager) extends ControllerState(roundManager) {
  override def eval(input: String): Unit = {
    val in = Controller.toInt(input)
    if (in.isEmpty) return
    if (roundManager.predictionMode) roundManager.updatePlayerPrediction(in.get)
    else roundManager.evaluate(in.get)
  }

  /*override def evalPlayerPrediction(input: String): Unit = {
    val prediction = Controller.toInt(input)
    if(prediction.isEmpty) return
    roundManager.updatePlayerPrediction(prediction.get)
  }*/

  override def getCurrentStateAsString: String = roundManager.getPlayerStateStrings

  override def nextState: ControllerState = gameOverState(roundManager)
}


case class gameOverState(roundManager: RoundManager) extends ControllerState(roundManager) {
  override def eval(input: String): Unit = ()

  /*override def evalPlayerPrediction(input: String): Unit = ()*/

  override def getCurrentStateAsString: String = "\nGame Over! Press 'q' to quit."

  override def nextState: ControllerState = this
}