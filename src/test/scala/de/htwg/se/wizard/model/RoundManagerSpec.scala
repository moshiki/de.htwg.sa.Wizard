package de.htwg.se.wizard.model

import de.htwg.se.wizard.controller.{Controller, RoundManager}
import de.htwg.se.wizard.model.cards.{Card, CardStack, DefaultCard, JesterCard, WizardCard}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.ListBuffer

class RoundManagerSpec extends WordSpec with Matchers {
  "A Round Manager" when {
    "new" should {
      val resultTable = ResultTable(20, 3, ResultTable.initializeVector(3, 3))
      val roundManager = RoundManager(resultTable = resultTable)
      val controller = new Controller(roundManager)
      "set the number of players correctly" in {
        controller.roundManager = controller.roundManager.copy(numberOfPlayers = 3)
        controller.roundManager.checkNumberOfPlayers(3)
        controller.roundManager.numberOfPlayers should be(3)
      }
    }

    "has the number of players initialized" should {
      val resultTable = ResultTable(20, 3, ResultTable.initializeVector(3, 3))
      val roundManager = RoundManager(resultTable = resultTable)
      val controller = new Controller(roundManager)

      "with three Players" in {
        controller.roundManager = controller.roundManager.copy(numberOfPlayers = 3)
        controller.roundManager.roundsForThisGame should be(20)
      }

      "with four Players" in {
        controller.roundManager = controller.roundManager.copy(4)
        controller.roundManager.roundsForThisGame should be(15)
      }

      "with five Players" in {
        controller.roundManager = controller.roundManager.copy(5)
        controller.roundManager.roundsForThisGame should be(12)
      }

      "invalid amount of Players" in {
        an[IllegalArgumentException] shouldBe thrownBy(controller.roundManager.copy(6))
      }

    }
  }
    "controller is in setup mode" should {
      val resultTable = ResultTable(20, 3, ResultTable.initializeVector(3, 3))
      val roundManager = RoundManager(resultTable = resultTable)
      val controller = new Controller(roundManager)
      "ask for next player's name correctly" in {
        controller.roundManager = controller.roundManager.copy(currentPlayer = 1)
        controller.roundManager.getSetupStrings should be("Player 1, please enter your name:")
      }

      "get the next player correctly" in {
        controller.roundManager = controller.roundManager.copy(currentPlayer = 1, numberOfPlayers = 3)
        controller.roundManager.nextPlayerSetup should be(2)
      }

      "increment the player count up to the number provided by the user" in {
        controller.roundManager = controller.roundManager.copy(currentPlayer = 2, numberOfPlayers = 3)
        controller.roundManager.nextPlayerSetup should be(3)
      }
      "reset the player count when there's no next player" in {
        controller.roundManager = controller.roundManager.copy(currentPlayer = 3, numberOfPlayers = 3)
        controller.roundManager.nextPlayerSetup should be(0)
      }

      "add a player correctly to a list of all players" in {
        controller.roundManager = controller.roundManager.addPlayer("Name")
        controller.roundManager.players should be(List(Player("Name")))
      }

      "controller is in game mode" should {
        val resultTable = ResultTable(20, 3, ResultTable.initializeVector(20,3))
        val roundManager = RoundManager(resultTable = resultTable)
        val controller = new Controller(roundManager)

       /* "get the next player correctly" in {
          controller.roundManager = controller.roundManager.copy(currentPlayer = 2, numberOfPlayers = 3)
          controller.roundManager.nextPlayer should be(2)
        }*/
        /*
      "reset the player counter once all players played a card" in {
        roundManager.currentPlayer = 2
        roundManager.nextPlayer should be(0)
      }*/

      "should ask player for his prediction if Prediction list is empty" in {
        controller.roundManager = controller.roundManager.copy(numberOfPlayers = 3 ,currentPlayer = 1,currentRound = 1,
          players = List(Player("Name1"), Player("Name2"), Player("Name3")), predictionMode = true)
        controller.roundManager = controller.roundManager.cardDistribution()
        controller.roundManager.getPlayerStateStrings
        controller.roundManager.predictionPerRound.size should be(0)
      }

      "update predictionPerRound correctly" in {
        controller.roundManager = controller.roundManager.updatePlayerPrediction(3)
        controller.roundManager.predictionPerRound should be(List(3))
      }

     /* "empty predictionPerRound once a new round starts " in {
        val player = Player("Name1")
       /* controller.roundManager =  controller.roundManager.cardDistribution()
        controller.roundManager = controller.roundManager.copy(numberOfPlayers = 3 ,currentPlayer = 0,currentRound = 2,
          players = List(player, Player("Name2"), Player("Name3")), predictionMode = true)*/

        controller.roundManager = controller.roundManager.copy(predictionMode = true, players = List(player, Player("Name2"), Player("Name3")), currentPlayer = 0)
        controller.roundManager = controller.roundManager.cardDistribution()
        controller.roundManager = controller.roundManager.nextRound
        controller.roundManager =controller.roundManager.updatePlayerPrediction(1)
        controller.roundManager.predictionPerRound should be(List(1))
      }*/
        /*

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
      } */

        "move to next round" in {
          val player1 = Player("name1")
          val player2 = Player("name2")
          val player3 = Player("name3", playerCards = Some(Nil))
          controller.roundManager = controller.roundManager.copy(currentPlayer = 0, numberOfPlayers = 3,
            currentRound = 1, numberOfRounds = 20)
          controller.roundManager = controller.roundManager.addPlayer("name1")
          controller.roundManager = controller.roundManager.addPlayer("name2")
          controller.roundManager = controller.roundManager.addPlayer("name3")
          controller.roundManager = controller.roundManager.copy(players = List(player1, player2, player3),
            predictionPerRound = List(0, 0, 0))

          controller.roundManager = controller.roundManager.nextRound
          controller.roundManager.currentRound should be(2)
        }

      "not increase the current round when its not correct to do so" in {
        controller.roundManager = controller.roundManager.copy(numberOfPlayers = 3 ,currentPlayer = 1,currentRound = 1,
          players = List(Player("Name1"), Player("Name2"), Player("Name3")), predictionMode = true)
        controller.roundManager.nextRound
        controller.roundManager.currentRound should be(1)
      }

        "calculate points when player prediction was correct" in {
          RoundManager.calcPoints(2,2) should be(20)
        }

        "calculate points when player prediction was less than stitches" in {
          RoundManager.calcPoints(1,2) should be(0)
        }

        "calculate points where player prediction was higher than stitches" in {
          RoundManager.calcPoints(3,1) should be(-20)
        }

        "get right stitch in one cycle and delete played cards" in {
          val player1 = Player("name1")
          val player2 = Player("name2")
          val player3 = Player("name3")
          val card1 = Card.setOwner(JesterCard(), player1)
          val card2 = Card.setOwner(WizardCard(), player2)
          val card3 = Card.setOwner(DefaultCard("blue",3), player3)
          controller.roundManager = controller.roundManager.copy(playedCards = List[Card](card1,card2,card3),
            stitchesPerRound =Map("name1" -> 0, "name2" -> 1, "name3" -> 0))
          controller.roundManager = controller.roundManager.stitchInThisCycle
          controller.roundManager.playedCards should be(Nil)
          controller.roundManager.stitchesPerRound should be(Map("name2" -> 2, "name1" -> 0, "name3"-> 0))

        }

        "get correct string representation when it's players turn" in {
          val player1 = Player("name1", Some(List(JesterCard())))
          val player2 = Player("name2", Some(List(WizardCard())))
          val player3 = Player("name3", Some(List(DefaultCard("blue",3))))
          controller.roundManager = controller.roundManager.copy(currentPlayer = 1, currentRound = 2, numberOfPlayers = 3,
            players = List(player1, player2, player3), predictionPerRound = List(1,2,0))
          controller.roundManager.getPlayerStateStrings should be(
           "Round 2 - Player: name2" + "\n" +
           "Select one of the following cards:" + "\n" +
             "{ " + player2.playerCards.get.mkString + " }"
          )
        }

        "print result table when player 1 is on turn" in {
          val player1 = Player("name1", Some(List(JesterCard())))
          val player2 = Player("name2", Some(List(WizardCard())))
          val player3 = Player("name3", Some(List(DefaultCard("blue",3))))
          controller.roundManager = controller.roundManager.copy(currentPlayer = 0, currentRound = 1, numberOfPlayers = 3,
            players = List(player1, player2, player3), predictionPerRound = List(), shuffledCardStack = List(DefaultCard("blue",3)),
            stitchesPerRound = Map.empty)
          controller.roundManager.getPlayerStateStrings should be(
            """
              |#  Player  1  #  Player  2  #  Player  3  #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################
              |#      0      #      0      #      0      #
              |###########################################""".stripMargin
            + "\n" + "Round 1 - Player: name1" + "\n" +
            "Trump Color: blue" + "\n" +
              "Your Cards: " + "{ " + player1.playerCards.get.mkString + " }" + "\n" +
            "Enter the amount of stitches you think you will get: "
          )

        }

      "trigger the next state and return game over when game is over and resultTable" in {
        controller.roundManager = controller.roundManager.copy(numberOfPlayers = 3 ,currentPlayer = 0,currentRound = 20, numberOfRounds = 20)
        controller.roundManager.getPlayerStateStrings should be(
          "\nGame Over! Press 'q' to quit.\n" +
            """#  Player  1  #  Player  2  #  Player  3  #
###########################################
#      20      #      20      #      20      #
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
      }
    }
  }
  /*
  "A RoundManager Builder" when {
    "builds a correct RoundManager" in {
      val roundManager = RoundManager.Builder().withNumberOfPlayers(3).withNumberOfRounds(20).build()
      roundManager should be(RoundManager(3, 20))
    }
      }
    }*/
}
