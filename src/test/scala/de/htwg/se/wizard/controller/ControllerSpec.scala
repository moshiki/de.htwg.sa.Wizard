package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.{Player, ResultTable}
import de.htwg.se.wizard.model.cards.{Card, DefaultCard, JesterCard, WizardCard}
import de.htwg.se.wizard.util.Observer
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.ListBuffer

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
      controller.state = preSetupState(controller)
      controller.getCurrentStateAsString should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
    }

    "switches to the next state correctly" in {
      controller.state = preSetupState(controller)
      controller.nextState()
      controller.state should be(setupState(controller))
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
    val state = preSetupState(controller)
    "does nothing when trying to evaluate a string that's not a number" in {
      val old = roundManager
      state.evaluate("AAA")
      roundManager should be(old)
    }
    /*
    "does nothing when the number of PLayers is invalid" in {
      state.evaluate("8")
      roundManager should be(roundManager)
    }
    "set the number of players correctly" in {
      state.evaluate("3")
      roundManager.numberOfPlayers should be(3)
    }
    "register the controller in the new roundManager" in {
      state.evaluate("3")
      state.roundManager.subscribers contains controller should be (true)
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
      roundManager = state.roundManager
      state.nextState should be(setupState(roundManager))
    }
  }
  "A setupState" when {
    val roundManager = RoundManager(3)
    val state = setupState(roundManager)
    "adds a player correctly" in {
      state.evaluate("Name")
      roundManager.players.contains(Player("Name")) should be(true)
    }
    "return the correct state string" in {
      roundManager.players = Nil
      state.getCurrentStateAsString should be("Player 1, please enter your name:")
    }
    "return the correct next state" in {
      state.nextState should be(inGameState(roundManager))
    }
  }
  "A inGameState" when {
    val roundManager = RoundManager()
    val state = inGameState(roundManager)
    "does nothing when trying to evaluate a string that's not a number" in {
      val old = RoundManager()
      state.evaluate("AAA")
      roundManager should be(old)
    }
    "set playedCards correctly" in {
      state.evaluate("1")
      val player = Player("Name")
      roundManager.players = List[Player](player)
      var list = List[Card]()
      list = List[Card](DefaultCard("red",5))
      list = list ::: List[Card](DefaultCard("blue",1))
      player.playerCards = Some(list.to[ListBuffer])

      roundManager.playCard(1)
      roundManager.playedCards should be(List(DefaultCard("red",5)))
    }
    "return the correct state string of reading in the prediction" in {
      roundManager.currentRound = 2
      val player = Player("Name")
      roundManager.players = List[Player](player)
      player.playerCards = Some(ListBuffer(JesterCard(Some(player))))
      state.getCurrentStateAsString should startWith(
        """Round 2 - Player: Name
          |Select one of the following cards:
          |{ C:Jester }""".stripMargin)
    }
    "return the correct next state" in {
      state.nextState should be(gameOverState(roundManager))
    }
  }
  "A gameOverState" should {
    val state = gameOverState(RoundManager())
    "do nothing when evaluating" in {
      state.evaluate("5")
    }
    "return the correct state string" in {
      state.getCurrentStateAsString should be("\nGame Over! Press 'q' to quit.")
    }
    "return itself as the next state" in {
      state.nextState should be(state)
    }*/

  }
}
