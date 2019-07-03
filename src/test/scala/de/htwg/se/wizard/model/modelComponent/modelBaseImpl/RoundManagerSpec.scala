package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class RoundManagerSpec extends WordSpec with Matchers {
  "A Round Manager" when {
    "new" should {
      val resultTable = ResultTable.initializeTable(20, 3)
      val roundManager = RoundManager(resultTable = resultTable)
      val controller = new Controller(roundManager)
      "set the number of players correctly" in {
        controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3)
        controller.roundManager.checkNumberOfPlayers(3)
        controller.roundManager.numberOfPlayers should be(3)
      }

      "is able to store itself in an xml representation and restore successfully" in {
        val stitchesPerRound = Map("name1" -> 0, "name2" -> 1, "name3" -> 0)
        val rm = roundManager.addPlayer("P").copy(predictionPerRound = List(0, 1), cleanMap = stitchesPerRound)
        val xml = rm.toXML
        val roundManager2 = roundManager.fromXML(xml)
        roundManager2 should be(rm)
      }

      "is able to store itself in a json representation and restore successfully" in {
        val stitchesPerRound = Map("name1" -> 0, "name2" -> 1, "name3" -> 0)
        val rm = roundManager.addPlayer("P").copy(predictionPerRound = List(0, 1), cleanMap = stitchesPerRound)
        val json = Json.toJson(rm)
        val rm2 = json.validate[RoundManager].asOpt.get
        rm2 should be(rm)
      }
    }
  }
  "controller is in setup mode" should {
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager)
    "ask for next player's name correctly" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayer = 1)
      controller.roundManager.getSetupStrings should be("Player 1, please enter your name:")
    }

    "get the next player correctly" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayer = 1, numberOfPlayers = 3)
      controller.roundManager.asInstanceOf[RoundManager].nextPlayerSetup should be(2)
    }

    "increment the player count up to the number provided by the user" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayer = 2, numberOfPlayers = 3)
      controller.roundManager.asInstanceOf[RoundManager].nextPlayerSetup should be(3)
    }
    "reset the player count when there's no next player" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayer = 3, numberOfPlayers = 3)
      controller.roundManager.asInstanceOf[RoundManager].nextPlayerSetup should be(0)
    }

    "add a player correctly to a list of all players" in {
      controller.roundManager = controller.roundManager.addPlayer("Name")
      controller.roundManager.asInstanceOf[RoundManager].players should be(List(Player("Name")))
    }

    "dont add a player if his name got entered already" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(players = Nil)
      controller.roundManager = controller.roundManager.addPlayer("P1")
      val oldList = controller.roundManager.asInstanceOf[RoundManager].players
      controller.roundManager = controller.roundManager.addPlayer("P1")

      controller.roundManager.asInstanceOf[RoundManager].players should be(oldList)
    }
  }

  "controller is in game mode" should {
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager)

    "should ask player for his prediction if Prediction list is empty" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3, currentPlayer = 1, currentRound = 1,
        players = List(Player("Name1"), Player("Name2"), Player("Name3")), predictionMode = true,
        shuffledCardStack = List(DefaultCard("blue", 2), WizardCard(), JesterCard(), WizardCard()))
      controller.roundManager = controller.roundManager.cardDistribution
      controller.roundManager.getPlayerStateStrings
      controller.roundManager.asInstanceOf[RoundManager].predictionPerRound.size should be(0)
    }

    "update predictionPerRound correctly" in {
      controller.roundManager = controller.roundManager.updatePlayerPrediction(3)
      controller.roundManager.asInstanceOf[RoundManager].predictionPerRound should be(List(3))
    }

    "store who played the highest card in the current cycle" in {
      var roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(players = Nil, numberOfPlayers = 1, predictionMode = false,
        playedCards = List(WizardCard(Some(Player("1")))))
      roundManager = roundManager.addPlayer("1")

      roundManager = roundManager.nextPlayer
      roundManager.stitchesPerRound("1") should be(1)
    }

    "move to next round" in {
      val player1 = Player("name1")
      val player2 = Player("name2")
      val player3 = Player("name3", playerCards = Some(Nil))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayer = 0, numberOfPlayers = 3,
        currentRound = 1, numberOfRounds = 20)
      controller.roundManager = controller.roundManager.addPlayer("name1")
      controller.roundManager = controller.roundManager.addPlayer("name2")
      controller.roundManager = controller.roundManager.addPlayer("name3")
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(players = List(player1, player2, player3),
        predictionPerRound = List(0, 0, 0))

      controller.roundManager = controller.roundManager.nextRound
      controller.roundManager.currentRound should be(2)
    }

    "not increase the current round when its not correct to do so" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3, currentPlayer = 1, currentRound = 1,
        players = List(Player("Name1"), Player("Name2"), Player("Name3")), predictionMode = true)
      controller.roundManager.nextRound
      controller.roundManager.currentRound should be(1)
    }

    "calculate points when player prediction was correct" in {
      RoundManager.calcPoints(2, 2) should be(40)
    }

    "calculate points when player prediction was less than stitches" in {
      RoundManager.calcPoints(1, 2) should be(10)
    }

    "calculate points where player prediction was higher than stitches" in {
      RoundManager.calcPoints(3, 1) should be(-10)
    }

    "get right stitch in one cycle and delete played cards" in {
      val player1 = Player("name1")
      val player2 = Player("name2")
      val player3 = Player("name3")
      val card1 = Card.setOwner(JesterCard(), player1)
      val card2 = Card.setOwner(WizardCard(), player2)
      val card3 = Card.setOwner(DefaultCard("blue", 3), player3)
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(playedCards = List[Card](card1, card2, card3),
        stitchesPerRound = Map("name1" -> 0, "name2" -> 1, "name3" -> 0))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].stitchInThisCycle
      controller.roundManager.asInstanceOf[RoundManager].playedCards should be(Nil)
      controller.roundManager.asInstanceOf[RoundManager].stitchesPerRound should be(Map("name2" -> 2, "name1" -> 0, "name3" -> 0))

    }

    "get correct string representation when it's players turn" in {
      val player1 = Player("name1", Some(List(JesterCard())))
      val player2 = Player("name2", Some(List(WizardCard())))
      val player3 = Player("name3", Some(List(DefaultCard("blue", 3))))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayer = 1, currentRound = 2, numberOfPlayers = 3,
        players = List(player1, player2, player3), predictionPerRound = List(1, 2, 0))
      controller.roundManager.getPlayerStateStrings should be(
        "Round 2 - Player: name2" + "\n" +
          "Select one of the following cards:" + "\n" +
          "{ " + player2.playerCards.get.mkString + " }"
      )
    }

    "print result table when player 1 is on turn" in {
      val player1 = Player("name1", Some(List(JesterCard())))
      val player2 = Player("name2", Some(List(WizardCard())))
      val player3 = Player("name3", Some(List(DefaultCard("blue", 3))))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayer = 0, currentRound = 1, numberOfPlayers = 3,
        players = List(player1, player2, player3), predictionPerRound = List(), shuffledCardStack = List(DefaultCard("blue", 3)))
      controller.roundManager.getPlayerStateStrings startsWith
        """
          |#  Player  1  #  Player  2  #  Player  3  #
          |###########################################""".stripMargin

    }


    "trigger the next state and return game over when game is over and resultTable" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3, currentPlayer = 0, currentRound = 20, numberOfRounds = 20)
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

    "assign cards if no cards assigned or make no changes to already assigned cards when in prediction mode" in {
      var roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3, predictionMode = true, players = Nil)
      roundManager = roundManager.copy(shuffledCardStack = List(WizardCard(), WizardCard(), WizardCard()), currentRound = 1)
      roundManager = roundManager.addPlayer("P1")
      roundManager = roundManager.addPlayer("P2")
      roundManager = roundManager.addPlayer("P3")

      roundManager = roundManager.cardDistribution
      roundManager.players.head.getPlayerCards.size should be(1)

      val newRoundManager = roundManager.cardDistribution
      newRoundManager should be(roundManager)
    }
  }
}
