package de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling._
import akka.stream.ActorMaterializer
import com.google.inject.Inject
import de.htwg.sa.wizard.resultTable.util.{ArrayArrayIntContainer, InitializeTableArgumentContainer, UpdatePointsArgumentContainer}
import de.htwg.se.wizard.controller.controllerComponent.ControllerInterface
import de.htwg.se.wizard.model.dbComponent.DaoInterface
import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.util.UndoManager
import play.api.libs.json.Json

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}

class Controller @Inject()(var roundManager: ModelInterface, fileIOInterface: FileIOInterface, daoInterface: DaoInterface)
  extends ControllerInterface {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val undoManager = new UndoManager
  var state: ControllerState = PreSetupState(this)

  val resultTableHost: String = "http://" + sys.env.getOrElse("RESULTTABLEMODULE_HOST", "localhost:54251") +"/"
  val cardModuleHost: String = "http://" + sys.env.getOrElse("CARDMODULE_HOST", "localhost:1234") +"/"

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

  override def currentStateAsString: String = {
    val response = Http().singleRequest(Get(resultTableHost + "resultTable/table"))
    val tableStringFuture = response.flatMap(r => Unmarshal(r.entity).to[String])
    val tableString = Await.result(tableStringFuture, Duration.Inf)
    tableString + "\n" + state.currentStateAsString
  }

  override def currentStateAsHtml: String = state.currentStateAsString

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

  override def currentAmountOfTricks: Int = roundManager.currentAmountOfTricks

  override def playerPrediction: Int = roundManager.playerPrediction

  override def predictionMode: Boolean = roundManager.predictionMode

  override def currentRound: Int = roundManager.currentRound

  override def playedCardsAsString: List[String] = roundManager.playedCardsAsString

  override def currentPlayersCards: List[String] = roundManager.currentPlayersCards

  override def topOfStackCardString: String = roundManager.topOfStackCardString

  override def playersAsStringList: List[String] = roundManager.playersAsStringList

  override def save(): Unit = {
    daoInterface.save(roundManager, controllerStateAsString)
    Await.ready(Http().singleRequest(HttpRequest(uri = resultTableHost + "resultTable/save")), Duration.Inf)
    Await.ready(Http().singleRequest(HttpRequest(uri = cardModuleHost + "cardMod/save")), Duration.Inf)
    notifyObservers()
  }

  override def load(): Unit = {
    val returnFuture = daoInterface.load(roundManager)

    Await.ready(Http().singleRequest(HttpRequest(uri = resultTableHost + "resultTable/load")), Duration.Inf)
    Await.ready(Http().singleRequest(HttpRequest(uri = cardModuleHost + "cardMod/load")), Duration.Inf)

    val loadedState = Await.result(returnFuture, Duration.Inf)

    state = loadedState._2 match {
      case "PreSetupState" => PreSetupState(this)
      case "SetupState" => SetupState(this)
      case "InGameState" => InGameState(this)
      case "GameOverState" => GameOverState(this)
    }
    roundManager = loadedState._1
    notifyObservers()
  }

  override def resultArray: Array[Array[Any]] = {
    val response = Http().singleRequest(HttpRequest(uri = resultTableHost + "resultTable/pointArrayForView"))
    val jsonStringFuture = response.flatMap(r => Unmarshal(r.entity).to[String])
    val jsonString = Await.result(jsonStringFuture, Duration.Inf)
    val json = Json.parse(jsonString)
    val arrayContainer: ArrayArrayIntContainer = Json.fromJson(json)(ArrayArrayIntContainer.containerReads).get
    arrayContainer.array.map(innerArray => innerArray.toArray[Any])
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
  def currentStateAsString: String
  def nextState: ControllerState
}


case class PreSetupState(controller: Controller) extends ControllerState {
  import akka.http.scaladsl.client.RequestBuilding.Post
  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  override def evaluate(input: String): Unit = {
    val number = Controller.toInt(input)
    val actualNumber = number match {
      case Some(number) => number
      case None => return
    }
    if (!controller.roundManager.isNumberOfPlayersValid(actualNumber)) return
    val initializeTableContainer = InitializeTableArgumentContainer(controller.numberOfRounds(actualNumber), actualNumber)
    Http().singleRequest(Post(controller.resultTableHost + "resultTable/table", Json.toJson(initializeTableContainer).toString()))
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
  import akka.http.scaladsl.client.RequestBuilding.Put
  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  override def evaluate(input: String): Unit = {
    val convertedInput = Controller.toInt(input) match {
      case Some(number) => number
      case None => return
    }
    if (controller.roundManager.predictionMode) controller.roundManager = controller.roundManager.updatePlayerPrediction(convertedInput)
    else controller.roundManager = controller.roundManager.playCard(convertedInput)
    controller.roundManager = controller.roundManager.nextPlayer
    if (controller.roundManager.isTimeForNextRound) {
      val pointsForThisRound = controller.roundManager.pointsForThisRound
      val currentRound = controller.roundManager.currentRound
      val updatePointsArgumentContainer = UpdatePointsArgumentContainer(currentRound, pointsForThisRound)
      Http().singleRequest(Put(controller.resultTableHost + "resultTable/table", Json.toJson(updatePointsArgumentContainer).toString()))
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