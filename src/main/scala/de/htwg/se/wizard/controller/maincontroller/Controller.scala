package de.htwg.se.wizard.controller.maincontroller

import com.google.inject.Inject
import de.htwg.se.wizard.controller.ControllerInterface
import de.htwg.se.wizard.model.{ResultTableBuilderInterface, StaticCardInterface, StaticPlayerInterface}
import de.htwg.se.wizard.util.UndoManager

import scala.xml.Elem

class Controller @Inject()(var roundManager: RoundManager,
                           staticPlayerInterface: StaticPlayerInterface,
                           staticCardInterface: StaticCardInterface,
                           resultTableBuilderInterface: ResultTableBuilderInterface) extends ControllerInterface {
  val undoManager = new UndoManager

  var state: ControllerState = PreSetupState(this, staticPlayerInterface, staticCardInterface, resultTableBuilderInterface)

  def nextState(): Unit = state = state.nextState

  def gameToXML: Elem = {
    <Game>
      <state>
        {controllerStateAsString}
      </state>
      {roundManager.toXML}
    </Game>
  }

  override def saveGameXML(): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("WizardSaveGame.xml" ))
    pw.write(gameToXML.toString())
    pw.close()
    notifyObservers()
  }

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

  override def getCurrentPlayerNumber: Int = roundManager.currentPlayer

  override def getCurrentPlayerString: String = roundManager.players(roundManager.currentPlayer).toString

  override def getCurrentAmountOfStitches: Int = roundManager.stitchesPerRound(getCurrentPlayerString)

  override def getPlayerPrediction: Int = roundManager.predictionPerRound(getCurrentPlayerNumber)

  override def predictionMode: Boolean = roundManager.predictionMode

  override def currentRound: Int = roundManager.currentRound

  override def playedCardsAsString: List[String] = roundManager.playedCards.map(card => card.toString)

  override def currentPlayersCards: List[String] = roundManager.players(getCurrentPlayerNumber).getPlayerCards.get.map(card => card.toString)

  override def topOfStackCardString: String = roundManager.shuffledCardStack.head.toString

  override def playersAsStringList: List[String] = roundManager.players.map(player => player.toString)

  override def resultArray: Array[Array[Any]] = roundManager.resultTable.toAnyArray
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


case class PreSetupState(controller: Controller, playerInterface: StaticPlayerInterface,
                         cardInterface: StaticCardInterface, staticResultTableInterface: ResultTableBuilderInterface) extends ControllerState {
  override def evaluate(input: String): Unit = {
    val number = Controller.toInt(input)
    if (number.isEmpty) return
    if (!controller.roundManager.checkNumberOfPlayers(number.get)) return
    controller.roundManager = RoundStrategy.execute(number.get, playerInterface, cardInterface, staticResultTableInterface)
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
    if (controller.roundManager.currentRound == controller.roundManager.numberOfRounds &&
      controller.roundManager.currentPlayer == 0) {
      controller.nextState()
      return
    }

    if (controller.roundManager.predictionPerRound.size < controller.roundManager.numberOfPlayers) {
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