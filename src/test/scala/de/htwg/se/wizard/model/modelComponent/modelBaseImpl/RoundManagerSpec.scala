package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards._
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class RoundManagerSpec extends AnyWordSpec with Matchers with MockFactory {
  "A Round Manager" when {
    "new" should {
      val fileIOStub = stub[FileIOInterface]
      val resultTable = ResultTable.initializeTable(20, 3)
      val roundManager = RoundManager(resultTable = resultTable)
      val controller = new Controller(roundManager, fileIOStub)
      "set the number of players correctly" in {
        controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3)
        controller.roundManager.isNumberOfPlayersValid(3)
        controller.roundManager.numberOfPlayers should be(3)
      }

      "is able to store itself in an xml representation and restore successfully" in {
        val stitchesPerRound = Map("name1" -> 0, "name2" -> 1, "name3" -> 0)
        var rm = roundManager.addPlayer("P").copy(predictionPerRound = List(0, 1), cleanMap = stitchesPerRound)
        rm = rm.copy(playedCards = List(WizardCard(), JesterCard()))
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
    val fileIOStub = stub[FileIOInterface]
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager, fileIOStub)
    "ask for next player's name correctly" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayerNumber = 1)
      controller.roundManager.setupStrings should be("Player 1, please enter your name:")
    }

    "get the next player correctly" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayerNumber = 1, numberOfPlayers = 3)
      controller.roundManager.asInstanceOf[RoundManager].nextPlayerSetup should be(2)
    }

    "increment the player count up to the number provided by the user" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayerNumber = 2, numberOfPlayers = 3)
      controller.roundManager.asInstanceOf[RoundManager].nextPlayerSetup should be(3)
    }
    "reset the player count when there's no next player" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayerNumber = 3, numberOfPlayers = 3)
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
    val fileIOStub = stub[FileIOInterface]
    val resultTable = ResultTable.initializeTable(20, 3)
    val roundManager = RoundManager(resultTable = resultTable)
    val controller = new Controller(roundManager, fileIOStub)

    "should ask player for his prediction if Prediction list is empty" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3, currentPlayerNumber = 1, currentRound = 1,
        players = List(Player("Name1"), Player("Name2"), Player("Name3")), predictionMode = true,
        shuffledCardStack = List(DefaultCard("blue", 2), WizardCard(), JesterCard(), WizardCard()))
      controller.roundManager = controller.roundManager.cardDistribution
      controller.roundManager.playerStateStrings
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
      roundManager.tricksPerRound("1") should be(1)
    }

    "move to next round" in {
      val player1 = Player("name1")
      val player2 = Player("name2")
      val player3 = Player("name3", playerCards = Some(Nil))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayerNumber = 0, numberOfPlayers = 3,
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
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3, currentPlayerNumber = 1, currentRound = 1,
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
        tricksPerRound = Map("name1" -> 0, "name2" -> 1, "name3" -> 0))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].trickInThisCycle
      controller.roundManager.asInstanceOf[RoundManager].playedCards should be(Nil)
      controller.roundManager.asInstanceOf[RoundManager].tricksPerRound should be(Map("name2" -> 2, "name1" -> 0, "name3" -> 0))

    }

    "get correct string representation when it's players turn" in {
      val player1 = Player("name1", Some(List(JesterCard())))
      val player2 = Player("name2", Some(List(WizardCard())))
      val player3 = Player("name3", Some(List(DefaultCard("blue", 3))))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayerNumber = 1, currentRound = 2, numberOfPlayers = 3,
        players = List(player1, player2, player3), predictionPerRound = List(1, 2, 0))
      controller.roundManager.playerStateStrings should be(
        "Round 2 - Player: name2" + "\n" +
          "Select one of the following cards:" + "\n" +
          "{ " + player2.playerCards.get.mkString + " }"
      )
    }

    "print result table when player 1 is on turn" in {
      val player1 = Player("name1", Some(List(JesterCard())))
      val player2 = Player("name2", Some(List(WizardCard())))
      val player3 = Player("name3", Some(List(DefaultCard("blue", 3))))
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(currentPlayerNumber = 0, currentRound = 1, numberOfPlayers = 3,
        players = List(player1, player2, player3), predictionPerRound = List(), shuffledCardStack = List(DefaultCard("blue", 3)))
      controller.roundManager.playerStateStrings should startWith(
        """
          |┌──────────────────────────┬─────────────────────────┬─────────────────────────┐
          |│Player 1                  │Player 2                 │Player 3                 │
          |├──────────────────────────┼─────────────────────────┼─────────────────────────┤""".stripMargin)
    }

    "trigger the next state and return game over when game is over and resultTable" in {
      controller.roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3, currentPlayerNumber = 0, currentRound = 20, numberOfRounds = 20)
      controller.roundManager.playerStateStrings should be(
        "\nGame Over! Press 'q' to quit.\n" +
          """┌──────────────────────────┬─────────────────────────┬─────────────────────────┐
            |│Player 1                  │Player 2                 │Player 3                 │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│20                        │20                       │20                       │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |├──────────────────────────┼─────────────────────────┼─────────────────────────┤
            |│0                         │0                        │0                        │
            |└──────────────────────────┴─────────────────────────┴─────────────────────────┘""".stripMargin)
    }

    "assign cards if no cards assigned or make no changes to already assigned cards when in prediction mode" in {
      var roundManager = controller.roundManager.asInstanceOf[RoundManager].copy(numberOfPlayers = 3, predictionMode = true, players = Nil)
      val shuffledStack = CardStack.shuffleCards(CardStack.initialize)
      val expectedShuffledStack = shuffledStack.splitAt(3)._2
      roundManager = roundManager.copy(shuffledCardStack = shuffledStack, currentRound = 1)
      roundManager = roundManager.addPlayer("P1")
      roundManager = roundManager.addPlayer("P2")
      roundManager = roundManager.addPlayer("P3")

      roundManager = roundManager.cardDistribution
      roundManager.players.head.getPlayerCards.get.size should be(1)
      roundManager.players.head.getPlayerCards.get.head.ownerName should be(roundManager.players.head.name)
      roundManager.players(1).getPlayerCards.get.size should be(1)
      roundManager.players(1).getPlayerCards.get.head.ownerName should be(roundManager.players(1).name)
      roundManager.players(2).getPlayerCards.get.size should be(1)
      roundManager.players(2).getPlayerCards.get.head.ownerName should be(roundManager.players(2).name)
      roundManager.shuffledCardStack should be(expectedShuffledStack)

      val newRoundManager = roundManager.cardDistribution
      newRoundManager should be(roundManager)
    }
  }
}
