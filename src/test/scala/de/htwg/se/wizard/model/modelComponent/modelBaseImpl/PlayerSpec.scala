package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards.{Card, JesterCard, WizardCard}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" when {
    "new" should {
      val player = Player("Name")
      "have a name" in {
        player.name should be("Name")
      }
      "have a nice String representation" in {
        player.toString should be("Name")
      }
      "is able to store itself in an xml representation and restore successfully" in {
        val xml = player.toXML
        val newPlayer = Player.fromXML(xml)
        newPlayer should be(player)
      }

      "is able to store itself in a json representation and restore successfully" in {
        val json = Json.toJson(player)
        val player2 = json.validate[Player].asOpt.get
        player2 should be(player)
      }

      "has no cards" in {
        player.playerCards should be(Nil)
      }

      "get the correct String for his turn" in {
        val player = Player("TestPlayer")
        val list = List[Card](JesterCard(Some(player)))
        val player1 = player.copy(playerCards = list)
        Player.playerTurn(player1, 1) should startWith(
          """Round 1 - Player: TestPlayer
            |Select one of the following cards:
            |{ Jester }""".stripMargin
        )
      }

      "get correct string for stitches" in {
        val player = Player("TestPlayer")
        val list = List[Card](JesterCard(Some(player)))
        val player1 = player.copy(playerCards = list)
        Player.playerPrediction(player1, 1, Some("blue")) should startWith(
          """Round 1 - Player: TestPlayer
            |Trump Color: blue
            |Your Cards: { Jester }
            |Guess your amount of tricks: """.stripMargin)
      }
    }

    "owning some cards" should {
      val player = Player("P", List(WizardCard(), JesterCard()))
      "is able to store itself in an xml representation and restore successfully" in {
        val xml = player.toXML
        val newPlayer = Player.fromXML(xml)
        newPlayer should be(player)
      }

      "is able to store itself in a json representation and restore successfully" in {
        val json = Json.toJson(player)
        val player2 = json.validate[Player].asOpt.get
        player2 should be(player)
      }
    }
  }
}
