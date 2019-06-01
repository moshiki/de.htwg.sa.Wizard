package de.htwg.se.wizard.controller

import de.htwg.se.wizard.util.Observable

class Controller(var roundManager: RoundManager) extends Observable {

  var state: ControllerState = preSetupState(this)

  def eval(input: String): Unit = {
    state.evaluate(input)
    notifyObservers()
  }

  def getCurrentStateAsString: String = state.getCurrentStateAsString

  def nextState(): Unit = state = state.nextState

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
  def evaluate(input: String): Unit

  def getCurrentStateAsString: String

  def nextState: ControllerState
}


case class preSetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    val number = Controller.toInt(input)
    if (number.isEmpty) return
    if (!controller.roundManager.checkNumberOfPlayers(number.get)) return
    controller.roundManager = RoundStrategy.execute(number.get)
    controller.nextState()
  }

  override def getCurrentStateAsString: String = "Welcome to Wizard!\nPlease enter the number of Players[3-5]:"

  override def nextState: ControllerState = setupState(controller)
}


case class setupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    controller.roundManager = controller.roundManager.addPlayer(input)
    if (controller.roundManager.players.size == controller.roundManager.numberOfPlayers) {
      controller.roundManager = controller.roundManager.copy(cleanMap = controller.roundManager.stitchesPerRound)
      controller.nextState()
    }
  }

  override def getCurrentStateAsString: String = {
    controller.roundManager = controller.roundManager.copy(currentPlayer = controller.roundManager.nextPlayerSetup)
    controller.roundManager.getSetupStrings
  }

  override def nextState: ControllerState = inGameState(controller)
}


case class inGameState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    val in = Controller.toInt(input)
    if (in.isEmpty) return
    if (controller.roundManager.predictionMode) controller.roundManager = controller.roundManager.updatePlayerPrediction(in.get)
    else controller.roundManager = controller.roundManager.playCard(in.get)
  }

  override def getCurrentStateAsString: String = {
    controller.roundManager = controller.roundManager.nextPlayer

    if (!controller.roundManager.predictionMode) controller.roundManager = controller.roundManager.nextRound
    if (controller.roundManager.currentRound == controller.roundManager.roundsForThisGame &&
      controller.roundManager.currentPlayer == 0) {
      controller.nextState()
    }

    if(controller.roundManager.predictionPerRound.size < controller.roundManager.numberOfPlayers) {
      controller.roundManager = controller.roundManager.copy(predictionMode = true)
      controller.roundManager = controller.roundManager.cardDistribution()
    } else {
      controller.roundManager = controller.roundManager.copy(predictionMode = false)
    }

    controller.roundManager.getPlayerStateStrings
  }

  override def nextState: ControllerState = gameOverState(controller)
}


case class gameOverState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = ()

  override def getCurrentStateAsString: String = "\nGame Over! Press 'q' to quit."

  override def nextState: ControllerState = this
}