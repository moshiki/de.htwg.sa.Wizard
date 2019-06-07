package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.cards.{Card, JesterCard, WizardCard}
import de.htwg.se.wizard.model.{Player, ResultTable}
import de.htwg.se.wizard.util.Observer
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  "A Controller" when {
    val resultTable = ResultTable(20, 3, ResultTable.initializeVector(3, 3))
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager)
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
      controller.state = PreSetupState(controller)
      controller.getCurrentStateAsString should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
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
  }

  "A preSetupState" when {
    val resultTable = ResultTable(20, 3, ResultTable.initializeVector(3, 3))
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager)
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

    /*"register the controller in the new roundManager" in {
      state.evaluate("3")
      controller.roundManager
      state.controller
    }*/

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
  }

  "A SetupState" when {
    val resultTable = ResultTable(20, 3, ResultTable.initializeVector(3, 3))
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager)
    val state = SetupState(controller)

    "adds a player correctly" in {
      state.evaluate("Name")
      controller.roundManager.players.contains(Player("Name")) should be(true)
    }

    "reads in stitches per round" in {
      controller.roundManager = roundManager.copy(3, players = List(Player("Name1"), Player("Name2")))
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
    val resultTable = ResultTable(20, 3, ResultTable.initializeVector(20, 3))
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager)
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

    /*"set next state" in {
      controller.roundManager = controller.roundManager.copy(predictionMode = true, players = List(Player("Name1"), Player("Name2"), Player("Name3")), numberOfRounds = 20, currentPlayer = 0, currentRound = 20)
      controller.roundManager.cardDistribution()
      val originState = state
      controller.eval("5")
      originState should not be controller.state

    }*/


    "play card correctly" in {
      controller.roundManager = controller.roundManager.copy(predictionMode = false,
        players = List(Player("Name1"), Player("Name2"), Player("Name3")), currentPlayer = 0)
      controller.roundManager = controller.roundManager.cardDistribution()
      controller.eval("1")

      controller.roundManager.playedCards should not be Nil
    }

    "gets current state as string" in {
      val player = Player("Name2", Some(List(JesterCard())))
      val cardStack = List[Card](JesterCard(), WizardCard())
      controller.roundManager = controller.roundManager.copy(shuffledCardStack = cardStack, predictionMode = true,
        players = List(Player("Name1"), player, Player("Name3")), currentPlayer = 1, numberOfPlayers = 3)
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
    val resultTable = ResultTable(20, 3, ResultTable.initializeVector(20, 3))
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager)
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
