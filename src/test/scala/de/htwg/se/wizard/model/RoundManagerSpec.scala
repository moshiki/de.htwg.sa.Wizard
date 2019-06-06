package de.htwg.se.wizard.model

import de.htwg.se.wizard.controller.{Controller, RoundManager}
import de.htwg.se.wizard.model.cards.{Card, CardStack, JesterCard}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.ListBuffer

class RoundManagerSpec extends WordSpec with Matchers {
  "A Round Manager" when {
    "new" should {
      val resultTable = ResultTable(20, 3, ResultTable.initializeVector(3, 3))
      val roundManager = RoundManager(resultTable = resultTable)
      val controller = new Controller(roundManager)
      "set the number of players correctly" in {
        controller.roundManager =  controller.roundManager.copy(numberOfPlayers = 3)
        controller.roundManager.checkNumberOfPlayers(3)
        controller.roundManager.numberOfPlayers should be(3)
      }
    }
   /*
    "has the number of players initialized" should {
      val roundManager1 = RoundManager(3)
      "with three Players" in {
        roundManager1.roundsForThisGame should be(20)
      }
      val roundManager2 = RoundManager(4)
      "with four Players" in {
        roundManager2.roundsForThisGame should be(15)
      }

        val roundManager3 = RoundManager(5)
      "with five Players" in {
        roundManager3.roundsForThisGame should be(12)
      }
      "invalid amount of Players" in {
        an[IllegalArgumentException] shouldBe thrownBy(RoundManager(6))
      }
    }
    "controller is in setup mode" should {
      val roundManager = RoundManager(3)
      val controller = new Controller(roundManager)
      "ask for the next player's name correctly" in {
        roundManager.currentPlayer = 0
        roundManager.getSetupStrings should be("Player 1, please enter your name:")
      }
      "get the next player correctly" in {
        roundManager.currentPlayer = 0
        roundManager.nextPlayerSetup should be(1)
      }
      "increment the player count up to the number provided by the user" in {
        roundManager.currentPlayer = 2
        roundManager.nextPlayerSetup should be(3)
      }
      "reset the player count when there's no next player" in {
        roundManager.currentPlayer = 3
        roundManager.nextPlayerSetup should be(0)
      }
      "add a player correctly to a list of all players" in {
        roundManager.addPlayer("Name")
        roundManager.players should be(List(Player("Name")))
      }
      "trigger the next state when enough players entered their names" in {
        val oldState = controller.state
        roundManager.addPlayer("Player 2")
        roundManager.addPlayer("Player 3")
        controller.state should not equal oldState
      }
    }
    "controller is in game mode" should {
      val roundManager = RoundManager(3)
      val controller = new Controller(roundManager)

      "get the next player correctly" in {
        roundManager.currentPlayer = 0
        roundManager.nextPlayer should be(1)
      }
      "reset the player counter once all players played a card" in {
        roundManager.currentPlayer = 2
        roundManager.nextPlayer should be(0)
      }

      "should ask player for his prediction if Prediction list is empty" in {
        roundManager.players = List(Player("Name"), Player("P2"))
        roundManager.currentPlayer = 0
        roundManager.getPlayerStateStrings
        roundManager.predictionPerRound.size should be(0)
      }

      "update predictionPerRound correctly" in {
        roundManager.updatePlayerPrediction(3)
        roundManager.predictionPerRound should be(List(3))
      }

      "empty predictionPerRound once a new round starts " in {
        roundManager.currentPlayer = 0
        roundManager.predictionPerRound = Nil
        roundManager.updatePlayerPrediction(1)
        roundManager.predictionPerRound should be(List(1))
      }

      "return the correct state string once all players told their prediction" in {
        roundManager.currentRound = 1
        roundManager.predictionPerRound = List(2)
        roundManager.predictionPerRound = roundManager.predictionPerRound ::: List(1)
        roundManager.predictionPerRound = roundManager.predictionPerRound ::: List(1)
        val player = Player("Name")
        roundManager.players = List[Player](player)
        player.playerCards = Some(ListBuffer(JesterCard(Some(player))))
        roundManager.getPlayerStateStrings should startWith(
          """Round 1 - Player: Name
Select one of the following cards:""".stripMargin)
      }

      "not increase the current round when its not correct to do so" in {
        roundManager.predictionPerRound = Nil
        roundManager.predictionMode = true
        roundManager.currentPlayer = 0
        roundManager.currentRound = 1
        roundManager.getPlayerStateStrings
        roundManager.currentRound should be(1)
      }
      "trigger the next state and return game over when game is over and resultTable" in {
        roundManager.predictionPerRound = Nil
        val oldState = controller.state
        roundManager.currentPlayer = 2
        roundManager.currentRound = 20
        roundManager.getPlayerStateStrings should be(
          "\nGame Over! Press 'q' to quit.\n" +
            """#  Player  1  #  Player  2  #  Player  3  #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################
#      0      #      0      #      0      #
###########################################""")


        controller.state should not equal oldState
      }
    }
  }
  "A RoundManager Builder" when {
    "builds a correct RoundManager" in {
      val roundManager = RoundManager.Builder().withNumberOfPlayers(3).withNumberOfRounds(20).build()
      roundManager should be(RoundManager(3, 20))
    }*/
  }
}
