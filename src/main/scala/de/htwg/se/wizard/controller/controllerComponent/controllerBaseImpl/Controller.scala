package de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl

import com.google.inject.Inject
import de.htwg.se.wizard.controller.controllerComponent.ControllerInterface
import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.util.UndoManager

import scala.util.{Failure, Success}

class Controller @Inject()(var roundManager: ModelInterface,
                           fileIOInterface: FileIOInterface,
                           var resultTableController: de.htwg.sa.wizard.controller.controllerComponent.ResultTableControllerInterface)
  extends ControllerInterface {

  val undoManager = new UndoManager
  var state: ControllerState = PreSetupState(this)

  def numberOfRounds(numberOfPlayers: Int): Int = numberOfPlayers match {
    case 3 => 20
    case 4 => 15
    case 5 => 12
  }

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

  override def currentStateAsString: String = state.currentStateAsString

  override def controllerStateAsString: String = {
    state match {
      case _: PreSetupState => "PreSetupState"
      case _: SetupState => "SetupState"
      case _: InGameState => "InGameState"
      case _: GameOverState => "GameOverState"
    }
  }

  override def currentPlayerNumber: Int = roundManager.currentPlayerNumber

  override def currentPlayerString: String = roundManager.currentPlayerString

  override def currentAmountOfStitches: Int = roundManager.currentAmountOfTricks

  override def playerPrediction: Int = roundManager.playerPrediction

  override def predictionMode: Boolean = roundManager.predictionMode

  override def currentRound: Int = roundManager.currentRound

  override def playedCardsAsString: List[String] = roundManager.playedCardsAsString

  override def currentPlayersCards: List[String] = roundManager.currentPlayersCards

  override def topOfStackCardString: String = roundManager.topOfStackCardString

  override def playersAsStringList: List[String] = roundManager.playersAsStringList

  override def save(): Unit = {
    fileIOInterface.save(controllerStateAsString, roundManager)
    notifyObservers()
  }

  override def load(): Unit = {
    val ret = fileIOInterface.load(roundManager)
    val returnTuple = ret match {
      case Failure(_) => return
      case Success(stateTuple) => stateTuple
    }
    state = returnTuple._1 match {
      case "PreSetupState" => PreSetupState(this)
      case "SetupState" => SetupState(this)
      case "InGameState" => InGameState(this)
      case "GameOverState" => GameOverState(this)
    }
    roundManager = returnTuple._2
    notifyObservers()
  }

  override def resultArray: Array[Array[Any]] = resultTableController.pointArrayForView
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
  def currentStateAsString: String
  def nextState: ControllerState
}


case class PreSetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    val number = Controller.toInt(input)
    val actualNumber = number match {
      case Some(number) => number
      case None => return
    }
    if (!controller.roundManager.isNumberOfPlayersValid(actualNumber)) return
    controller.resultTableController.initializeTable(controller.numberOfRounds(actualNumber), actualNumber)
    controller.roundManager = controller.roundManager.configurePlayersAndRounds(actualNumber)
    controller.nextState()
    controller.roundManager = controller.roundManager.nextPlayerInSetup
  }
  override def currentStateAsString: String = "Welcome to Wizard!\nPlease enter the number of Players[3-5]:"
  override def nextState: ControllerState = SetupState(controller)
}

case class SetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    if (input.isEmpty) return
    controller.roundManager = controller.roundManager.nextPlayerInSetup
    controller.roundManager = controller.roundManager.addPlayer(input)
    if (controller.roundManager.createdPlayers == controller.roundManager.numberOfPlayers) {
      controller.roundManager = controller.roundManager.saveCleanMap
      controller.roundManager = controller.roundManager.invokePredictionMode()
      controller.roundManager = controller.roundManager.cardDistribution
      controller.nextState()
    }
  }
  override def currentStateAsString: String = controller.roundManager.setupStrings
  override def nextState: ControllerState = InGameState(controller)
}

case class InGameState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    val in = Controller.toInt(input)
    val convertedInput = in match {
      case Some(number) => number
      case None => return
    }
    if (controller.roundManager.predictionMode) controller.roundManager = controller.roundManager.updatePlayerPrediction(convertedInput)
    else controller.roundManager = controller.roundManager.playCard(convertedInput)
    controller.roundManager = controller.roundManager.nextPlayer
    if (controller.roundManager.isTimeForNextRound) {
      val pointsForThisRound = controller.roundManager.pointsForThisRound
      val currentRound = controller.roundManager.currentRound
      controller.resultTableController.updatePoints(currentRound, pointsForThisRound)
      controller.roundManager = controller.roundManager.nextRound
    }
    if (controller.roundManager.currentRound == controller.roundManager.numberOfRounds &&
      controller.roundManager.currentPlayerNumber == 0) {
      controller.nextState()
      return
    }
    if (controller.roundManager.recordedPredictions < controller.roundManager.numberOfPlayers) {
      controller.roundManager = controller.roundManager.invokePredictionMode()
      controller.roundManager = controller.roundManager.cardDistribution
    } else {
      controller.roundManager = controller.roundManager.leavePredictionMode
    }
  }
  override def currentStateAsString: String = controller.roundManager.playerStateStrings
  override def nextState: ControllerState = GameOverState(controller)
}

case class GameOverState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = ()
  override def currentStateAsString: String = "\nGame Over! Press 'q' to quit."
  override def nextState: ControllerState = this
}