package de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject}
import de.htwg.se.wizard.WizardModule
import de.htwg.se.wizard.controller.controllerComponent.ControllerInterface
import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.util.UndoManager

class Controller @Inject()(var roundManager: ModelInterface, fileIOInterface: FileIOInterface) extends ControllerInterface {
  //val fileIOInterface: FileIOInterface = Guice.createInjector(new WizardModule).getInstance(classOf[FileIOInterface])

  val undoManager = new UndoManager

  var state: ControllerState = PreSetupState(this)

  def nextState(): Unit = state = state.nextState

  override def eval(input: String): Unit = {
    undoManager.doStep(new EvalStep(this))
    state.evaluate(input)
    notifyObservers()
  }

  override def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  override def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers()
  }

  override def getCurrentStateAsString: String = state.getCurrentStateAsString

  override def controllerStateAsString: String = {
    state match {
      case _: PreSetupState => "PreSetupState"
      case _: SetupState => "SetupState"
      case _: InGameState => "InGameState"
      case _: GameOverState => "GameOverState"
    }
  }

  override def getCurrentPlayerNumber: Int = roundManager.getCurrentPlayerNumber

  override def getCurrentPlayerString: String = roundManager.getCurrentPlayerString

  override def getCurrentAmountOfStitches: Int = roundManager.getCurrentAmountOfStitches

  override def getPlayerPrediction: Int = roundManager.getPlayerPrediction

  override def predictionMode: Boolean = roundManager.predictionMode

  override def currentRound: Int = roundManager.currentRound

  override def playedCardsAsString: List[String] = roundManager.playedCardsAsString

  override def currentPlayersCards: List[String] = roundManager.currentPlayersCards

  override def topOfStackCardString: String = roundManager.topOfStackCardString

  override def playersAsStringList: List[String] = roundManager.playersAsStringList

  override def resultArray: Array[Array[Any]] = roundManager.resultArray

  override def save(): Unit = {
    fileIOInterface.save(controllerStateAsString, roundManager)
    notifyObservers()
  }

  override def load(): Unit = {
    val ret = fileIOInterface.load(roundManager)
    state = ret._1 match {
      case "PreSetupState" => PreSetupState(this)
      case "SetupState" => SetupState(this)
      case "InGameState" => InGameState(this)
      case "GameOverState" => GameOverState(this)
    }
    roundManager = ret._2
    notifyObservers()
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
  def evaluate(input: String): Unit

  def getCurrentStateAsString: String

  def nextState: ControllerState
}


case class PreSetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    val number = Controller.toInt(input)
    if (number.isEmpty) return
    if (!controller.roundManager.checkNumberOfPlayers(number.get)) return
    controller.roundManager = controller.roundManager.setPlayersAndRounds(number.get)
    controller.nextState()

    controller.roundManager = controller.roundManager.nextPlayerInSetup
  }

  override def getCurrentStateAsString: String = "Welcome to Wizard!\nPlease enter the number of Players[3-5]:"

  override def nextState: ControllerState = SetupState(controller)
}


case class SetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    if (input.isEmpty) return

    controller.roundManager = controller.roundManager.nextPlayerInSetup

    controller.roundManager = controller.roundManager.addPlayer(input)
    if (controller.roundManager.createdPlayers == controller.roundManager.numberOfPlayers) {
      controller.roundManager = controller.roundManager.saveCleanMap

      controller.roundManager = controller.roundManager.setPredictionMode
      controller.roundManager = controller.roundManager.cardDistribution
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
    if (controller.roundManager.currentRound == controller.roundManager.numberOfRounds &&
      controller.roundManager.getCurrentPlayerNumber == 0) {
      controller.nextState()
      return
    }

    if (controller.roundManager.recordedPredictions < controller.roundManager.numberOfPlayers) {
      controller.roundManager = controller.roundManager.setPredictionMode
      controller.roundManager = controller.roundManager.cardDistribution
    } else {
      controller.roundManager = controller.roundManager.unsetPredictionMode
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