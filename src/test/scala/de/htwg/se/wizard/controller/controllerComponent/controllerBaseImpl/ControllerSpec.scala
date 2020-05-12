package de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards._
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.{Player, ResultTable, RoundManager}
import de.htwg.se.wizard.util.Observer
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Success

class ControllerSpec extends AnyWordSpec with Matchers with MockFactory {
  "A Controller" when {
    val fileIOStub = stub[FileIOInterface]
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager, fileIOStub)
    val observer = stub[Observer]

    controller.add(observer)
    "notify its observer after evaluating an input string" in {
      controller.eval("4")
      (observer.update _).verify().once
    }

    "gets the correct string depending of the current state" in {
      controller.state = PreSetupState(controller)
      controller.currentStateAsString should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
    }

    "switches to the next state correctly" in {
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
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayerNumber = 0)
      controller.currentPlayerNumber should be(0)
    }

    "returns true if game asks for players predictions" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(predictionMode = true)
      controller.predictionMode should be(true)
    }

    "returns the name of a player" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(players = List(Player("test")), currentPlayerNumber = 0)
      controller.currentPlayerString should be("test")
    }

    "returns the current players prediction" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(predictionPerRound = List(5),
        players = List(Player("test")), currentPlayerNumber = 0)
      controller.playerPrediction should be(5)
    }

    "returns the current players amount of stitches" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(tricksPerRound = Map("test" -> 2),
        players = List(Player("test")), currentPlayerNumber = 0)
      controller.currentAmountOfStitches should be(2)
    }

    "return the current round" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentRound = 20)
      controller.currentRound should be(20)
    }

    "convert the already played cards to a list of strings" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(playedCards = List(WizardCard(), JesterCard()))
      controller.playedCardsAsString should be(List(WizardCard().toString, JesterCard().toString))
    }

    "convert the current players cards to a list of strings" in {
      val player = Player("player", Some(List(JesterCard(), WizardCard())))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(players = List(player), currentPlayerNumber = 0)
      controller.currentPlayersCards should be(List(JesterCard().toString, WizardCard().toString))
    }

    "return a string representation of the top card on the shuffled card stack" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(shuffledCardStack = List(DefaultCard("blue", 2), WizardCard()))
      controller.topOfStackCardString should be(DefaultCard("blue", 2).toString)
    }

    "return a list with all players string representations" in {
      val playerList = List(Player("P1"), Player("P2"))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(players = playerList, currentPlayerNumber = 0)
      controller.playersAsStringList should be(List("P1", "P2"))
    }

    "save and restore the whole game" in {
      val roundManagerStub = stub[ModelInterface]
      val fileIOMock = mock[FileIOInterface]
      fileIOMock.save _ expects("PreSetupState", roundManagerStub)
      fileIOMock.load _ expects roundManagerStub returning Success("PreSetupState",roundManagerStub)
      val controller = new Controller(roundManagerStub, fileIOMock)
      controller.save()
      controller.load()
      controller.state should be(PreSetupState(controller))
      controller.roundManager should be(roundManagerStub)
    }

    "save and restore the whole game with all four possible controller states" in {
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
  }

  "A preSetupState" when {
    val fileIOStub = stub[FileIOInterface]
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager, fileIOStub)
    val state = PreSetupState(controller)
    "does nothing when trying to evaluate a string that's not a number" in {
      val old = roundManager
      state.evaluate("AAA")
      roundManager should be(old)
    }

    "does nothing when the number of PLayers is invalid" in {
      state.evaluate("8")
      roundManager should be(roundManager)
    }

    "set the number of players correctly" in {
      state.evaluate("3")
      val newRoundManager = RoundManager(3, resultTable = resultTable)
      newRoundManager.numberOfPlayers should be(3)
    }

    "trigger the controller to switch to the next state" in {
      val old = state
      state.evaluate("3")
      controller.state should not be old
    }

    "return the correct state string" in {
      state.currentStateAsString should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
    }


    "return the correct next state" in {
      state.nextState should be(SetupState(controller))
    }

    "return the same result array stored in ResultTable" in {
      controller.resultArray should be(controller.roundManager.asInstanceOf[RoundManager].resultTable.toAnyArray)
    }
  }

  "A SetupState" when {
    val fileIOStub = stub[FileIOInterface]
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager, fileIOStub)
    val state = SetupState(controller)

    "does nothing when theres no input" in {
      val oldRM = controller.roundManager
      state.evaluate("")
      controller.roundManager should be(oldRM)
    }

    "adds a player correctly" in {
      state.evaluate("Name")
      controller.roundManager.asInstanceOf[RoundManager].players.contains(Player("Name")) should be(true)
    }

    "reads in stitches per round" in {
      controller.roundManager = roundManager.copy(3, players = List(Player("Name1"), Player("Name2")),
        shuffledCardStack = List(DefaultCard("blue", 2), WizardCard(), WizardCard(), WizardCard()))
      state.evaluate("Name3")
      controller.roundManager.numberOfPlayers should be(3)
      controller.roundManager.asInstanceOf[RoundManager].players.size should be(3)
      controller.roundManager.asInstanceOf[RoundManager].cleanMap should be(Map("Name3" -> 0))
    }

    "set predictionMode true" in {
      controller.roundManager.predictionMode should be(true)
    }

    "return the correct state string" in {
      state.currentStateAsString should be("Player 1, please enter your name:")
    }

    "return the correct next state" in {
      state.nextState should be(InGameState(controller))
    }
  }

  "A InGameState" when {
    val fileIOStub = stub[FileIOInterface]
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager, fileIOStub)
    controller.state = InGameState(controller)
    "does nothing when trying to evaluate a string that's not a number" in {
      controller.eval("AAA")
      roundManager should be(roundManager)
    }

    "update player prediction" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(predictionMode = true, players = List(Player("Name1"), Player("Name2"), Player("Name3")))
      controller.eval("1")
      controller.roundManager.asInstanceOf[RoundManager].predictionPerRound should be(List(1))
    }

    "stay to prediction mode" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3, numberOfRounds = 20, predictionMode = true,
       shuffledCardStack = List(DefaultCard("blue", 2), WizardCard(), WizardCard(), WizardCard()), players = Nil)
      controller.roundManager = controller.roundManager.addPlayer("1")
      controller.roundManager = controller.roundManager.addPlayer("2")
      controller.roundManager = controller.roundManager.addPlayer("3")
      controller.eval("0")

      controller.roundManager.predictionMode should be(true)
    }

    "play card correctly" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(predictionMode = false, numberOfPlayers = 2,
        players = Nil, currentPlayerNumber = 0, currentRound = 1, predictionPerRound = List(0, 0))
      controller.roundManager = controller.roundManager.addPlayer("1")
      controller.roundManager = controller.roundManager.addPlayer("2")
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(
        players = List(Player("1", playerCards = Some(List(WizardCard(owner = Some(Player("1")))))),
          Player("2", playerCards = Some(Nil))))
      controller.eval("1")

      controller.roundManager.asInstanceOf[RoundManager].playedCards should not be Nil
    }

    "gets current state as string" in {
      val player = Player("Name2", Some(List(JesterCard())))
      val cardStack = List[Card](JesterCard(), WizardCard())
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(shuffledCardStack = cardStack, predictionMode = true,
        players = List(Player("Name1"), player, Player("Name3")), currentPlayerNumber = 1, numberOfPlayers = 3, currentRound = 1)
      val card = player.playerCards.get
      controller.currentStateAsString should be(
        s"""Round 1 - Player: Name2
           |Trump Color: None
           |Your Cards: { ${card.mkString} }
           |Guess your amount of tricks: """.stripMargin
      )
    }

    "trigger the next state in controller" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 2, numberOfRounds = 1,
        currentRound = 1, currentPlayerNumber = 1, players = List(Player("1"), Player("2")))
      val oldState = controller.state
      controller.eval("1")
      controller.state should be(oldState.nextState)
    }
  }

  "A GameOverState" should {
    val fileIOStub = stub[FileIOInterface]
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable)
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
