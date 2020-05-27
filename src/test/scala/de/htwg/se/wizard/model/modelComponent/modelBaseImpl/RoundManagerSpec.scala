package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RoundManagerSpec extends AnyWordSpec with Matchers with MockFactory {
  /*"A Round Manager" when {
    "new" should {
      val roundManager = RoundManager()
      "set the number of players correctly" in {
        val newRoundManager = roundManager.copy(numberOfPlayers = 3)
        newRoundManager.isNumberOfPlayersValid(3)
        newRoundManager.numberOfPlayers should be(3)
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

  "A RoundManager" should {

    "ask for next player's name correctly" in {
      val roundManager = RoundManager(currentPlayerNumber = 1)
      roundManager.setupStrings should be("Player 1, please enter your name:")
    }

    "get the next player correctly" in {
      val roundManager = RoundManager(currentPlayerNumber = 1, numberOfPlayers = 3)
      roundManager.nextPlayerSetup should be(2)
    }

    "increment the player count up to the number provided by the user" in {
      val roundManager = RoundManager(currentPlayerNumber = 2, numberOfPlayers = 3)
      roundManager.nextPlayerSetup should be(3)
    }
    "reset the player count when there's no next player" in {
      val roundManager = RoundManager(currentPlayerNumber = 3, numberOfPlayers = 3)
      roundManager.nextPlayerSetup should be(0)
    }

    "add a player correctly to a list of all players" in {
      val roundManager = RoundManager().addPlayer("Name")
      roundManager.players should be(List(Player("Name")))
    }

    "dont add a player if his name got entered already" in {
      val expectedList = List(Player("P1"))
      val roundManager = RoundManager(players = expectedList)

      val newRoundManager = roundManager.addPlayer("P1")
      newRoundManager.players should be(expectedList)
    }


    "should ask player for his prediction if Prediction list is empty" in {
      // FIXME: No idea what this test is used for. Description does not match code.
      // Probably does not need to be fixed after creation of modules.

      val roundManager = RoundManager(numberOfPlayers = 3, currentPlayerNumber = 1,
        players = List(Player("Name1"), Player("Name2"), Player("Name3")),
        shuffledCardStack = List(DefaultCard("blue", 2), WizardCard(), JesterCard(), WizardCard()))
      val newRoundManager = roundManager.cardDistribution
      newRoundManager.playerStateStrings // FIXME: not used value (see note from above)
      newRoundManager.predictionPerRound.size should be(0)
    }

    "update predictionPerRound correctly" in {
      val roundManager = RoundManager().updatePlayerPrediction(3)
      roundManager.predictionPerRound should be(List(3))
    }

    "store who played the highest card in the current cycle" in {
      var roundManager = RoundManager(players = Nil, numberOfPlayers = 1, predictionMode = false,
        playedCards = List(WizardCard(Some("1"))))
      roundManager = roundManager.addPlayer("1")

      roundManager = roundManager.nextPlayer
      roundManager.tricksPerRound("1") should be(1)
    }

    "move to next round" in {
      val player1 = Player("name1")
      val player2 = Player("name2")
      val player3 = Player("name3", playerCards = Nil)
      val roundManager = RoundManager(numberOfPlayers = 3, numberOfRounds = 20,
        players = List(player1, player2, player3), predictionPerRound = List(0, 0, 0))
      /*controller.roundManager = controller.roundManager.addPlayer("name1")
      controller.roundManager = controller.roundManager.addPlayer("name2")
      controller.roundManager = controller.roundManager.addPlayer("name3")*/

      val newRoundManager = roundManager.nextRound
      newRoundManager.currentRound should be(2)
    }

    "not increase the current round when its not correct to do so" in {
      val roundManager = RoundManager(numberOfPlayers = 3, currentPlayerNumber = 1,
        players = List(Player("Name1"), Player("Name2"), Player("Name3")))
      val newRoundManager = roundManager.nextRound
      newRoundManager.currentRound should be(1)
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
      val card1 = JesterCard().setOwner(player1.name)
      val card2 = WizardCard().setOwner(player2.name)
      val card3 = DefaultCard("blue", 3) setOwner player3.name
      val roundManager = RoundManager(playedCards = List[CardInterface](card1, card2, card3),
        tricksPerRound = Map("name1" -> 0, "name2" -> 1, "name3" -> 0))
      val newRoundManager = roundManager.trickInThisCycle
      newRoundManager.playedCards should be(Nil)
      newRoundManager.tricksPerRound should be(Map("name2" -> 2, "name1" -> 0, "name3" -> 0))

    }

    "get correct string representation when it's players turn" in {
      val player1 = Player("name1", List(JesterCard()))
      val player2 = Player("name2", List(WizardCard()))
      val player3 = Player("name3", List(DefaultCard("blue", 3)))
      val roundManager = RoundManager(currentPlayerNumber = 1, currentRound = 2, numberOfPlayers = 3,
        players = List(player1, player2, player3), predictionPerRound = List(1, 2, 0))
      roundManager.playerStateStrings should be(
        s"""Round 2 - Player: name2
           |Select one of the following cards:
           |{ ${player2.playerCards.mkString} }""".stripMargin
      )
    }

    "trigger the next state and return game over when game is over and resultTable" in {
      val roundManager = RoundManager(numberOfPlayers = 3, currentRound = 20, numberOfRounds = 20)
      roundManager.playerStateStrings should be("\nGame Over! Press 'q' to quit.\n")
    }

    "assign cards if no cards assigned or make no changes to already assigned cards when in prediction mode" in {
      // TODO clean this one up (if not obsolete due to modularization)
      var roundManager = RoundManager(numberOfPlayers = 3, players = Nil)
      val shuffledStack = CardStack.shuffleCards(CardStack.initialize)
      val expectedShuffledStack = shuffledStack.splitAt(3)._2
      roundManager = roundManager.copy(shuffledCardStack = shuffledStack, currentRound = 1)
      roundManager = roundManager.addPlayer("P1")
      roundManager = roundManager.addPlayer("P2")
      roundManager = roundManager.addPlayer("P3")

      roundManager = roundManager.cardDistribution
      roundManager.players.head.playerCards.size should be(1)
      roundManager.players.head.playerCards.head.ownerName should be(roundManager.players.head.name)
      roundManager.players(1).playerCards.size should be(1)
      roundManager.players(1).playerCards.head.ownerName should be(roundManager.players(1).name)
      roundManager.players(2).playerCards.size should be(1)
      roundManager.players(2).playerCards.head.ownerName should be(roundManager.players(2).name)
      roundManager.shuffledCardStack should be(expectedShuffledStack)

      val newRoundManager = roundManager.cardDistribution
      newRoundManager should be(roundManager)
    }

    "calculate the points for each player correctly after one round" in {
      val players = List(Player("P1"), Player("P2"))
      val tricksPerRound = Map("P1" -> 0, "P2" -> 0)
      val predictionPerRound = List(0, 0)
      val roundManager = RoundManager(players = players, tricksPerRound = tricksPerRound, predictionPerRound = predictionPerRound)
      val expectedResult = Vector(20, 20)

      roundManager.pointsForThisRound should be(expectedResult)
    }
  }*/
}
