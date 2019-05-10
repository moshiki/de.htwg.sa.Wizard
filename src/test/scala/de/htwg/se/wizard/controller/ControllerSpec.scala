package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.{Player, RoundManager}
import de.htwg.se.wizard.util.Observer
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  "A Controller" when {
    val roundManager = new RoundManager
    val controller = new Controller(roundManager)
    val observer = new Observer {
      var updated: Boolean = false

      def isUpdated: Boolean = updated

      override def update(): Unit = updated = true
    }
    controller.add(observer)
    "observed by an Observer" should {
      "should return the welcome message after initialisation" in {
        controller.getCurrentState should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
      }
      "evaluate the input correctly, so that the number of players does not get set if input is not a number" in {
        controller.eval("bla")
        roundManager.numberOfPlayers should be(0)
      }
      "evaluate the input correctly, so that the number of players gets set if input is a number" in {
        controller.eval("5")
        roundManager.numberOfPlayers should be(5)
      }
      "ask for the players name in setup mode" in {
        roundManager.numberOfPlayers = 3
      }
      "update the players when in setup mode" in {
        roundManager.needsSetup = true
        roundManager.numberOfPlayers = 3
        controller.eval("name")
        roundManager.players.head.name should be("name")
      }
      "return the correct status String" in {
        controller.getCurrentState should be("Player 1, please enter your name:")
      }
      "get the current players round String when in game" in {
        roundManager.needsSetup = false
        roundManager.players = List(Player("Name", 0))
        roundManager.numberOfPlayers = 3
        roundManager.currentPlayer = 2
        controller.getCurrentState should startWith
        """
           Round 1 - Player 1 (test1)
           Select one of the following cards:
        """.stripMargin
      }
      "notify its Observer after evaluating an input string" in {
        roundManager.needsSetup = true
        roundManager.numberOfPlayers = 0
        controller.eval("4")
        observer.updated should be(true)
        controller.roundManager.numberOfPlayers should be(4)
      }
      "notify its Observer after evaluating an input string in normal mode" in {
        roundManager.needsSetup = false
        controller.eval("4")
        observer.updated should be(true)
      }
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
}
