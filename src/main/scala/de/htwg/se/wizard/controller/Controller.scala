package de.htwg.se.wizard.controller

import de.htwg.se.wizard.util.{Observable, UndoManager}

class Controller(var roundManager: RoundManager) extends Observable {
  val undoManager = new UndoManager

  var state: ControllerState = PreSetupState(this)

  def eval(input: String): Unit = {
    undoManager.doStep(new EvalStep(this))
    state.evaluate(input)
    notifyObservers()
  }

  def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    undoManager.redoStep()
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


case class PreSetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    val number = Controller.toInt(input)
    if (number.isEmpty) return
    if (!controller.roundManager.checkNumberOfPlayers(number.get)) return
    controller.roundManager = RoundStrategy.execute(number.get)
    controller.nextState()

    controller.roundManager = controller.roundManager.copy(currentPlayer = controller.roundManager.nextPlayerSetup)
  }

  override def getCurrentStateAsString: String = "Welcome to Wizard!\nPlease enter the number of Players[3-5]:"

  override def nextState: ControllerState = SetupState(controller)
}


case class SetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    if (input.isEmpty) return

    controller.roundManager = controller.roundManager.copy(currentPlayer = controller.roundManager.nextPlayerSetup)

    controller.roundManager = controller.roundManager.addPlayer(input)
    if (controller.roundManager.players.size == controller.roundManager.numberOfPlayers) {
      controller.roundManager = controller.roundManager.copy(cleanMap = controller.roundManager.stitchesPerRound)

      controller.roundManager = controller.roundManager.copy(predictionMode = true)
      controller.roundManager = controller.roundManager.cardDistribution()
      controller.nextState()
    }
  }

  override def getCurrentStateAsString: String = controller.roundManager.getSetupStrings

  override def nextState: ControllerState = InGameState(controller)
}


case class InGameState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    val in = Controller.toInt(input)
    if (in.isEmpty) return
    if (controller.roundManager.predictionMode) controller.roundManager = controller.roundManager.updatePlayerPrediction(in.get)
    else controller.roundManager = controller.roundManager.playCard(in.get)

    controller.roundManager = controller.roundManager.nextPlayer
    if (!controller.roundManager.predictionMode) controller.roundManager = controller.roundManager.nextRound
    if (controller.roundManager.currentRound == controller.roundManager.roundsForThisGame &&
      controller.roundManager.currentPlayer == 0) {
      controller.nextState()
      return
    }

    if(controller.roundManager.predictionPerRound.size < controller.roundManager.numberOfPlayers) {
      controller.roundManager = controller.roundManager.copy(predictionMode = true)
      controller.roundManager = controller.roundManager.cardDistribution()
    } else {
      controller.roundManager = controller.roundManager.copy(predictionMode = false)
    }
  }

  override def getCurrentStateAsString: String = controller.roundManager.getPlayerStateStrings

  override def nextState: ControllerState = GameOverState(controller)
}


case class GameOverState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = ()

  override def getCurrentStateAsString: String = "\nGame Over! Press 'q' to quit."

  override def nextState: ControllerState = this
}