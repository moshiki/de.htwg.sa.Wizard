package de.htwg.se.wizard

import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards.{Card, DefaultCard, JesterCard, WizardCard}
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.{Player, ResultTable, RoundManager}
import play.api.libs.json._

object jsontest {
  def main(args: Array[String]): Unit = {
    /*val resultTable = ResultTable.initializeTable()
    val json = Json.toJson(resultTable)
    val table2 = json.validate[ResultTable].asOpt.get

    println(resultTable == table2)*/

    /*val card = WizardCard(Some(Player("P1")))
    val json = card.toJson
    val card2 = Card.fromJson(json)
    println(card == card2)*/

    /*val card = JesterCard(Some(Player("P1")))
    val json = card.toJson
    val card2 = Card.fromJson(json)
    println(card == card2)*/

    /*val card = DefaultCard("red", 2, Some(Player("P1")))
    val json = card.toJson
    val card2 = Card.fromJson(json)
    println(card == card2)*/

    val roundManager = RoundManager(resultTable = ResultTable.initializeTable())
    val json = Json.toJson(roundManager)
    println(json)
    val rm2 = json.validate[RoundManager].asOpt.get

    println(json)
    println()
    println(roundManager == rm2)
  }
}
