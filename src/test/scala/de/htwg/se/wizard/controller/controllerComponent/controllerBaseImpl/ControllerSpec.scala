package de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import de.htwg.se.wizard.util.Observer
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.{Failure, Success}

class ControllerSpec extends AnyWordSpec with Matchers with MockFactory {

  "A Controller" when {
    "notify its observer after evaluating an input string" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      val observer = stub[Observer]
      controller.add(observer)
      controller.eval("4")

      (observer.update _).verify().once
    }

    /*"gets the correct string depending of the current state" in { FIXME: Not testable because of akka
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      controller.state = PreSetupState(controller)
      controller.currentStateAsString should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
    }*/

    "switches to the next state correctly" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      controller.state = PreSetupState(controller)
      controller.nextState()
      controller.state should be(SetupState(controller))
    }


    "can convert a string to a number correctly" should {
      "return an Int packed in Some when there is a number" in {
        val number = Controller.toInt("5")
        number.isDefined should be(true)
        number.get should be(5)
      }
      "return None when there is no number" in {
        val number = Controller.toInt("bla")
        number.isEmpty should be(true)
      }
    }

    "returns the current controller state as string representation" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)

      controller.state = PreSetupState(controller)
      controller.controllerStateAsString should be("PreSetupState")

      controller.state = SetupState(controller)
      controller.controllerStateAsString should be("SetupState")

      controller.state = InGameState(controller)
      controller.controllerStateAsString should be("InGameState")

      controller.state = GameOverState(controller)
      controller.controllerStateAsString should be("GameOverState")
    }

    "returns the current players number" in {
      val expectedPlayerNumber = 25
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.currentPlayerNumber _).expects() returning expectedPlayerNumber

      controller.currentPlayerNumber should be(expectedPlayerNumber)
    }

    "returns true if game asks for players predictions" in {
      val expectedPredictionMode = true
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.predictionMode _).expects() returning expectedPredictionMode

      controller.predictionMode should be(expectedPredictionMode)
    }

    "returns the name of a player" in {
      val expectedPlayerName = "Player"
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.currentPlayerString _).expects() returning expectedPlayerName

      controller.currentPlayerString should be(expectedPlayerName)
    }

    "returns the current players prediction" in {
      val expectedPlayersPrediction = 25
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.playerPrediction _).expects() returning expectedPlayersPrediction

      controller.playerPrediction should be(expectedPlayersPrediction)
    }

    "returns the current players amount of tricks" in {
      val expectedAmountOfTricks = 25
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.currentAmountOfTricks _).expects() returning expectedAmountOfTricks

      controller.currentAmountOfTricks should be(expectedAmountOfTricks)
    }

    "return the current round" in {
      val expectedRound = 25
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.currentRound _).expects() returning expectedRound

      controller.currentRound should be(expectedRound)
    }

    "return the already played cards in a list" in {
      val expectedPlayedCards = List("Card1", "Card2")
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.playedCardsAsString _).expects() returning expectedPlayedCards

      controller.playedCardsAsString should be(expectedPlayedCards)
    }

    "return the current players cards to a list of strings" in {
      val expectedCurrentPlayerCards = List("Card1", "Card2")
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.currentPlayersCards _).expects() returning expectedCurrentPlayerCards

      controller.currentPlayersCards should be(expectedCurrentPlayerCards)
    }

    "return a string representation of the top card on the shuffled card stack" in {
      val expectedTopOfCardStack = "Card"
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.topOfStackCardString _).expects() returning expectedTopOfCardStack

      controller.topOfStackCardString should be(expectedTopOfCardStack)
    }

    "return a list with all players string representations" in {
      val expectedPlayerStrings = List("Player1", "Player2")
      val fileIOStub = stub[FileIOInterface]
      val roundManager = mock[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (roundManager.playersAsStringList _).expects() returning expectedPlayerStrings
      controller.playersAsStringList should be(expectedPlayerStrings)
    }

    "save and restore the whole game" in { // FIXME No tests for akka client code
      val roundManagerStub = stub[ModelInterface]
      val expectedRoundMangerStub = stub[ModelInterface]
      val fileIOMock = mock[FileIOInterface]
      fileIOMock.save _ expects("InGameState", roundManagerStub)
      fileIOMock.load _ expects roundManagerStub returning Success("PreSetupState",expectedRoundMangerStub)
      val controller = new Controller(roundManagerStub, fileIOMock)
      controller.state = InGameState(controller)
      controller.save()
      controller.load()
      controller.state should be(PreSetupState(controller))
      controller.roundManager should be(expectedRoundMangerStub)
    }

    "save and restore the whole game with all four possible controller states" in { // FIXME creating a server instance of ResultTableModuleHttpServer
      val roundManagerStub = stub[ModelInterface]
      val fileIOMock = mock[FileIOInterface]
      fileIOMock.save _ expects("PreSetupState", roundManagerStub)
      fileIOMock.load _ expects roundManagerStub returning Success("PreSetupState",roundManagerStub)

      val controller = new Controller(roundManagerStub, fileIOMock)
      var state = controller.state
      controller.save()
      controller.load()
      controller.state should be(state)

      fileIOMock.save _ expects("SetupState", roundManagerStub)
      fileIOMock.load _ expects roundManagerStub returning Success("SetupState",roundManagerStub)

      state = SetupState(controller)
      controller.state = state
      controller.save()
      controller.load()
      controller.state should be(state)

      fileIOMock.save _ expects("InGameState", roundManagerStub)
      fileIOMock.load _ expects roundManagerStub returning Success("InGameState",roundManagerStub)

      state = InGameState(controller)
      controller.state = state
      controller.save()
      controller.load()
      controller.state should be(state)

      fileIOMock.save _ expects("GameOverState", roundManagerStub)
      fileIOMock.load _ expects roundManagerStub returning Success("GameOverState",roundManagerStub)

      state = GameOverState(controller)
      controller.state = state
      controller.save()
      controller.load()
      controller.state should be(state)
    }

    "does nothing when an error occurs while loading the game" in {
      val roundManagerStub = stub[ModelInterface]
      val fileIOMock = mock[FileIOInterface]
      fileIOMock.load _ expects roundManagerStub returning Failure(new Exception())
      val controller = new Controller(roundManagerStub, fileIOMock)
      controller.load()
      controller.state should be(PreSetupState(controller))
      controller.roundManager should be(roundManagerStub)
    }

    "calculates the rounds correctly depending on the number of players" in {
      val roundManagerStub = stub[ModelInterface]
      val fileIOMock = mock[FileIOInterface]
      val controller = new Controller(roundManagerStub, fileIOMock)
      controller.numberOfRounds(3) should be(20)
      controller.numberOfRounds(4) should be(15)
      controller.numberOfRounds(5) should be(12)
    }
  }

  "A preSetupState" when {
    "does nothing when trying to evaluate a string that's not a number" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      val state = PreSetupState(controller)
      val old = roundManager
      state.evaluate("AAA")
      roundManager should be(old)
    }

    "does nothing when the number of PLayers is invalid" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      val state = PreSetupState(controller)
      state.evaluate("8")
    }

    "set the number of players correctly and set the dimensions of the resultTable" in { // FIXME creating a server instance of ResultTableModuleHttpServer
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      val state = PreSetupState(controller)
      //(resultTableControllerMock.initializeTable _).expects(20, 3).returning(resultTableControllerMock)
      state.evaluate("3")
      val newRoundManager = RoundManager(3)
      newRoundManager.numberOfPlayers should be(3)
    }

    "trigger the controller to switch to the next state and set the dimensions of the resultTable" in { // FIXME creating a server instance of ResultTableModuleHttpServer
      val expactedNumber = 3
      val fileIOStub = stub[FileIOInterface]
      val roundManagerMock = mock[ModelInterface]
      val controller = new Controller(roundManagerMock, fileIOStub)
      val state = PreSetupState(controller)
      controller.state = state
      //(resultTableControllerMock.initializeTable _).expects(20, 3).returning(resultTableControllerMock)
      (roundManagerMock.isNumberOfPlayersValid _).expects(expactedNumber) returning true
      (roundManagerMock.configurePlayersAndRounds _).expects(expactedNumber) returning roundManagerMock
      (roundManagerMock.nextPlayerInSetup _).expects() returning roundManagerMock
      val old = state
      state.evaluate("3")
      controller.state should not be old
    }

    "return the correct state string" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      val state = PreSetupState(controller)
      state.currentStateAsString should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
    }


    "return the correct next state" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      val state = PreSetupState(controller)
      state.nextState should be(SetupState(controller))
    }

    /*"return the same result array stored in ResultTable" in { // FIXME: Not testable because of akka
      val expectedArray = Array(Array(1))
      val fileIOStub = stub[FileIOInterface]
      val resultTableControllerMock = mock[ResultTableControllerInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      (resultTableControllerMock.pointArrayForView _).expects().returning(expectedArray)
      controller.resultArray should be(expectedArray)
    }*/
  }

  "A SetupState" when {
    "does nothing when there is no input" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManagerStub = stub[ModelInterface]
      val controller = new Controller(roundManagerStub, fileIOStub)
      val state = SetupState(controller)
      state.evaluate("")
    }

    "adds a player correctly as long as not all players were created" in {
      val expectedName = "Name"
      val fileIOStub = stub[FileIOInterface]
      val roundManagerMock = mock[ModelInterface]
      val controller = new Controller(roundManagerMock, fileIOStub)
      val state = SetupState(controller)
      inSequence {
        (roundManagerMock.nextPlayerInSetup _).expects() returning roundManagerMock
        (roundManagerMock.addPlayer _).expects(expectedName) returning roundManagerMock
        (roundManagerMock.createdPlayers _).expects() returning 1
        (roundManagerMock.numberOfPlayers _).expects() returning 3
      }
      state.evaluate(expectedName)
    }

    "prepares and invokes the next state when all players were created" in {
      val expectedName = "Name"
      val fileIOStub = stub[FileIOInterface]
      val roundManagerMock = mock[ModelInterface]
      val expectedRoundManagerMock = mock[ModelInterface]
      val controller = new Controller(roundManagerMock, fileIOStub)
      val state = SetupState(controller)
      controller.state = state
      inSequence {
        (roundManagerMock.nextPlayerInSetup _).expects() returning roundManagerMock
        (roundManagerMock.addPlayer _).expects(expectedName) returning expectedRoundManagerMock
        (expectedRoundManagerMock.createdPlayers _).expects() returning 3
        (expectedRoundManagerMock.numberOfPlayers _).expects() returning 3
        (expectedRoundManagerMock.saveCleanMap _).expects() returning expectedRoundManagerMock
        (expectedRoundManagerMock.invokePredictionMode _).expects() returning expectedRoundManagerMock
        (expectedRoundManagerMock.cardDistribution _).expects() returning expectedRoundManagerMock
      }

      controller.state.evaluate(expectedName)
      controller.state should be(state.nextState)
    }
  }

  "A InGameState" when {
    "does nothing when trying to evaluate a string that's not a number" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManager = stub[ModelInterface]
      val controller = new Controller(roundManager, fileIOStub)
      controller.state = InGameState(controller)
      controller.eval("AAA")
    }

    "updates the player's prediction and invokes switching to the next player when the game is in prediction mode" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManagerMock = mock[ModelInterface]
      val controller = new Controller(roundManagerMock, fileIOStub)
      val state = InGameState(controller)
      inSequence {
        (roundManagerMock.predictionMode _).expects() returning true
        (roundManagerMock.updatePlayerPrediction _).expects(1).returning(roundManagerMock)
        (roundManagerMock.nextPlayer _).expects().returning(roundManagerMock)
        (roundManagerMock.isTimeForNextRound _).expects().returning(false)
        (roundManagerMock.currentRound _).expects().returning(1)
        (roundManagerMock.numberOfRounds _).expects().returning(42)
        (roundManagerMock.recordedPredictions _).expects().returning(1)
        (roundManagerMock.numberOfPlayers _).expects().returning(42)
        (roundManagerMock.invokePredictionMode _).expects() returning roundManagerMock
        (roundManagerMock.cardDistribution _).expects() returning roundManagerMock
      }
      state.evaluate("1")
    }

    "invokes playing a card when not in prediction mode" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManagerMock = mock[ModelInterface]
      val controller = new Controller(roundManagerMock, fileIOStub)
      val state = InGameState(controller)
      inSequence {
        (roundManagerMock.predictionMode _).expects() returning false
        (roundManagerMock.playCard _).expects(1) returning roundManagerMock
        (roundManagerMock.nextPlayer _).expects().returning(roundManagerMock)
        (roundManagerMock.isTimeForNextRound _).expects().returning(false)
        (roundManagerMock.currentRound _).expects().returning(1)
        (roundManagerMock.numberOfRounds _).expects().returning(42)
        (roundManagerMock.recordedPredictions _).expects().returning(42)
        (roundManagerMock.numberOfPlayers _).expects().returning(42)
        (roundManagerMock.leavePredictionMode _).expects() returning(roundManagerMock)
      }
      state.evaluate("1")
    }

    "gets current state as string correctly" in {
      val expectedStateString = "State"
      val fileIOStub = stub[FileIOInterface]
      val roundManagerMock = mock[ModelInterface]
      val controller = new Controller(roundManagerMock, fileIOStub)
      val state = InGameState(controller)
      (roundManagerMock.playerStateStrings _).expects().returning(expectedStateString)
      state.currentStateAsString should be(expectedStateString)
    }

    "trigger the next state in controller when there are no rounds to play anymore and it's the last players turn" in {
      val fileIOStub = stub[FileIOInterface]
      val roundManagerMock = mock[ModelInterface]
      val controller = new Controller(roundManagerMock, fileIOStub)
      controller.state = InGameState(controller)
      inSequence {
        (roundManagerMock.predictionMode _).expects() returning false
        (roundManagerMock.playCard _).expects(1) returning roundManagerMock
        (roundManagerMock.nextPlayer _).expects().returning(roundManagerMock)
        (roundManagerMock.isTimeForNextRound _).expects().returning(false)
        (roundManagerMock.currentRound _).expects().returning(33)
        (roundManagerMock.numberOfRounds _).expects().returning(33)
        (roundManagerMock.currentPlayerNumber _).expects().returning(0)
      }
      val oldState = controller.state
      controller.state.evaluate("1")
      controller.state should be(oldState.nextState)
    }

    "switches to next round if possible and stores the points gained in this round" in { // FIXME
      val pointsForThisRound = Vector(1)
      val currentRound = 2
      val fileIOStub = stub[FileIOInterface]
      val roundManagerStub = stub[ModelInterface]
      val expectedRoundManagerStub = stub[ModelInterface]
      val controller = new Controller(roundManagerStub, fileIOStub)
      controller.state = InGameState(controller)
      (roundManagerStub.isTimeForNextRound _).when().returns(true)
      (roundManagerStub.pointsForThisRound _).when().returns(pointsForThisRound)
      (roundManagerStub.currentRound _ ).when().returns(currentRound)
      (roundManagerStub.predictionMode _).when().returns(true)
      (roundManagerStub.nextPlayer _).when().returns(roundManagerStub)
      (roundManagerStub.updatePlayerPrediction _).when(*).returns(roundManagerStub)
      (roundManagerStub.numberOfRounds _).when().returns(200)
      (roundManagerStub.nextRound _).when().returns(expectedRoundManagerStub)
      //(resultTableControllerMock.updatePoints _).expects(currentRound, pointsForThisRound)

      controller.state.evaluate("3")
      controller.roundManager should be(expectedRoundManagerStub)
    }
  }

  "A GameOverState" should {
    val fileIOStub = stub[FileIOInterface]
    val roundManager = RoundManager()
    val controller = new Controller(roundManager, fileIOStub)
    val state = GameOverState(controller)
    "do nothing when evaluating" in {
      state.evaluate("5")
    }

    "return the correct state string" in {
      state.currentStateAsString should be("\nGame Over! Press 'q' to quit.")

    }

    "return itself as the next state" in {
      state.nextState should be(state)
    }
  }
}
