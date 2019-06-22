package de.htwg.se.wizard.controller.maincontroller

import de.htwg.se.wizard.model.modelComponent.cards.{Card, DefaultCard, JesterCard, WizardCard}
import de.htwg.se.wizard.model.modelComponent.{Player, ResultTable}
import de.htwg.se.wizard.util.Observer
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  "A Controller" when {
    val cardInterface = Card
    val playerInterface = Player
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable, playerInterface = playerInterface, cardInterface = cardInterface)
    val controller = new Controller(roundManager, playerInterface, cardInterface, ResultTable)
    val observer = new Observer { // wontfix
      var updated: Boolean = false

      def isUpdated: Boolean = updated

      override def update(): Unit = updated = true
    }
    controller.add(observer)
    "notify its observer after evaluating an input string" in {
      controller.eval("4")
      observer.updated should be(true)
    }

    "gets the correct string depending of the current state" in {
      controller.state = PreSetupState(controller, playerInterface, cardInterface, ResultTable)
      controller.getCurrentStateAsString should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
    }

    "switches to the next state correctly" in {
      controller.state = PreSetupState(controller, playerInterface, cardInterface, ResultTable)
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
      controller.state = PreSetupState(controller, playerInterface, cardInterface, ResultTable)
      controller.controllerStateAsString should be("PreSetupState")

      controller.state = SetupState(controller)
      controller.controllerStateAsString should be("SetupState")

      controller.state = InGameState(controller)
      controller.controllerStateAsString should be("InGameState")

      controller.state = GameOverState(controller)
      controller.controllerStateAsString should be("GameOverState")
    }

    "returns the current players number" in {
      controller.roundManager = controller.roundManager.copy(currentPlayer = 0)
      controller.getCurrentPlayerNumber should be(0)
    }

    "returns true if game asks for players predictions" in {
      controller.roundManager = controller.roundManager.copy(predictionMode = true)
      controller.predictionMode should be(true)
    }

    "returns the name of a player" in {
      controller.roundManager = controller.roundManager.copy(players = List(new Player("test")), currentPlayer = 0)
      controller.getCurrentPlayerString should be("test")
    }

    "returns the current players prediction" in {
      controller.roundManager = controller.roundManager.copy(predictionPerRound = List(5),
        players = List(new Player("test")), currentPlayer = 0)
      controller.getPlayerPrediction should be(5)
    }

    "returns the current players amount of stitches" in {
      controller.roundManager = controller.roundManager.copy(stitchesPerRound = Map("test" -> 2),
        players = List(new Player("test")), currentPlayer = 0)
      controller.getCurrentAmountOfStitches should be(2)
    }

    "return the current round" in {
      controller.roundManager = controller.roundManager.copy(currentRound = 20)
      controller.currentRound should be(20)
    }

    "convert the already played cards to a list of strings" in {
      controller.roundManager = controller.roundManager.copy(playedCards = List(WizardCard(), JesterCard()))
      controller.playedCardsAsString should be(List(WizardCard().toString(), JesterCard().toString()))
    }

    "convert the current players cards to a list of strings" in {
      val player = Player("player", Some(List(JesterCard(), WizardCard())))
      controller.roundManager = controller.roundManager.copy(players = List(player), currentPlayer = 0)
      controller.currentPlayersCards should be(List(JesterCard().toString(), WizardCard().toString()))
    }

    "return a string representation of the top card on the shuffled card stack" in {
      controller.roundManager = controller.roundManager.copy(shuffledCardStack = List(DefaultCard("blue", 2), WizardCard()))
      controller.topOfStackCardString should be(DefaultCard("blue", 2).toString)
    }

    "return a list with all players string representations" in {
      val playerList = List(Player("P1"), Player("P2"))
      controller.roundManager = controller.roundManager.copy(players = playerList, currentPlayer = 0)
      controller.playersAsStringList should be(List("P1", "P2"))
    }
  }

  "A preSetupState" when {
    val cardInterface = Card
    val playerInterface = Player
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable, playerInterface = playerInterface, cardInterface = cardInterface)
    val controller = new Controller(roundManager, playerInterface, cardInterface, ResultTable)
    val state = PreSetupState(controller, playerInterface, cardInterface, ResultTable)
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
      val newRoundManager = RoundManager(3, resultTable = resultTable, playerInterface = playerInterface, cardInterface = cardInterface)
      newRoundManager.numberOfPlayers should be(3)
    }

    "trigger the controller to switch to the next state" in {
      val old = state
      state.evaluate("3")
      controller.state should not be old
    }

    "return the correct state string" in {
      state.getCurrentStateAsString should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
    }


    "return the correct next state" in {
      state.nextState should be(SetupState(controller))
    }

    "return the same result array stored in ResultTable" in {
      controller.resultArray should be(controller.roundManager.resultTable.toAnyArray)
    }
  }

  "A SetupState" when {
    val cardInterface = Card
    val playerInterface = Player
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable, playerInterface = playerInterface, cardInterface = cardInterface)
    val controller = new Controller(roundManager, playerInterface, cardInterface, ResultTable)
    val state = SetupState(controller)

    "does nothing when theres no input" in {
      val oldRM = controller.roundManager
      state.evaluate("")
      controller.roundManager should be(oldRM)
    }

    "adds a player correctly" in {
      state.evaluate("Name")
      controller.roundManager.players.contains(Player("Name")) should be(true)
    }

    "reads in stitches per round" in {
      controller.roundManager = roundManager.copy(3, players = List(Player("Name1"), Player("Name2")),
        shuffledCardStack = List(DefaultCard("blue", 2), WizardCard(), WizardCard(), WizardCard()))
      state.evaluate("Name3")
      controller.roundManager.numberOfPlayers should be(3)
      controller.roundManager.players.size should be(3)
      controller.roundManager.cleanMap should be(Map("Name3" -> 0))
    }

    "set predictionMode true" in {
      controller.roundManager.predictionMode should be(true)
    }

    "return the correct state string" in {
      state.getCurrentStateAsString should be("Player 1, please enter your name:")
    }

    "return the correct next state" in {
      state.nextState should be(InGameState(controller))
    }
  }

  "A InGameState" when {
    val cardInterface = Card
    val playerInterface = Player
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable, playerInterface = playerInterface, cardInterface = cardInterface)
    val controller = new Controller(roundManager, playerInterface, cardInterface, ResultTable)
    controller.state = InGameState(controller)
    "does nothing when trying to evaluate a string that's not a number" in {
      controller.eval("AAA")
      roundManager should be(roundManager)
    }

    "update player prediction" in {
      controller.roundManager = controller.roundManager.copy(predictionMode = true, players = List(Player("Name1"), Player("Name2"), Player("Name3")))
      controller.eval("1")
      controller.roundManager.predictionPerRound should be(List(1))
    }

    "stay to prediction mode" in {
      controller.roundManager = controller.roundManager.copy(numberOfPlayers = 3, numberOfRounds = 20, predictionMode = true,
       shuffledCardStack = List(DefaultCard("blue", 2), WizardCard(), WizardCard(), WizardCard()), players = Nil)
      controller.roundManager = controller.roundManager.addPlayer("1")
      controller.roundManager = controller.roundManager.addPlayer("2")
      controller.roundManager = controller.roundManager.addPlayer("3")
      controller.eval("0")

      controller.roundManager.predictionMode should be(true)
    }

    "play card correctly" in {
      controller.roundManager = controller.roundManager.copy(predictionMode = false, numberOfPlayers = 2,
        players = Nil, currentPlayer = 0, currentRound = 1, predictionPerRound = List(0, 0))
      controller.roundManager = controller.roundManager.addPlayer("1")
      controller.roundManager = controller.roundManager.addPlayer("2")
      controller.roundManager = controller.roundManager.copy(
        players = List(Player("1", playerCards = Some(List(WizardCard(owner = Some(Player("1")))))),
          Player("2", playerCards = Some(Nil))))
      controller.eval("1")

      controller.roundManager.playedCards should not be Nil
    }

    "gets current state as string" in {
      val player = Player("Name2", Some(List(JesterCard())))
      val cardStack = List[Card](JesterCard(), WizardCard())
      controller.roundManager = controller.roundManager.copy(shuffledCardStack = cardStack, predictionMode = true,
        players = List(Player("Name1"), player, Player("Name3")), currentPlayer = 1, numberOfPlayers = 3, currentRound = 1)
      val card = player.playerCards.get
      controller.getCurrentStateAsString should startWith(
        "\n" + "Round 1 - Player: Name2" + "\n" +
          "Trump Color: None" + "\n" +
          "Your Cards: " + "{ " + card.mkString + " }" + "\n" +
          "Enter the amount of stitches you think you will get: "
      )
    }

    "trigger the next state in controller" in {
      controller.roundManager = controller.roundManager.copy(numberOfPlayers = 2, numberOfRounds = 1,
        currentRound = 1, currentPlayer = 1, players = List(Player("1"), Player("2")))
      val oldState = controller.state
      controller.eval("1")
      controller.state should be(oldState.nextState)
    }
  }

  "A GameOverState" should {
    val cardInterface = Card
    val playerInterface = Player
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable, playerInterface = playerInterface, cardInterface = cardInterface)
    val controller = new Controller(roundManager, playerInterface, cardInterface, ResultTable)
    val state = GameOverState(controller)
    "do nothing when evaluating" in {
      state.evaluate("5")
    }
    "return the correct state string" in {
      state.getCurrentStateAsString should be("\nGame Over! Press 'q' to quit.")

    }
    "return itself as the next state" in {
      state.nextState should be(state)
    }

  }
}
