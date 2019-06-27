package de.htwg.se.wizard

import de.htwg.se.wizard.controller.maincontroller.RoundManager
import de.htwg.se.wizard.model.modelComponent.cards.{DefaultCard, JesterCard, StaticCard, WizardCard}
import de.htwg.se.wizard.model.modelComponent.{Player, ResultTable, ResultTableBuilder, StaticPlayer}

object xmltest {
  def main(args: Array[String]): Unit = {
    /*val player = Player("P1")//, playerCards = Some(List(WizardCard(), JesterCard())))
    val playerXML = player.toXML

    println(playerXML.toString())
    println()

    val newPlayer = StaticPlayer().fromXML(playerXML)
    println(newPlayer)*/

    /*val card = DefaultCard("red", 5, owner = Some(Player("P1")))
    val xml = card.toXML
    //println(xml)
    val card2 = StaticCard().fromXML(xml)
    println(card2)*/

    /*var table = ResultTableBuilder().initializeTable(2, 2)
    table = table.updatePoints(1, 1,1)
    val xml = table.toXML

    val table2 = ResultTableBuilder().fromXML(xml)
    println(table2)*/

    /*var roundManager = RoundManager.Builder().build(StaticPlayer(), StaticCard(), ResultTableBuilder())
    roundManager = roundManager.addPlayer("P1")
    roundManager = roundManager.addPlayer("P2")
    roundManager = roundManager.addPlayer("P3")
    roundManager = roundManager.copy(numberOfPlayers = 3, numberOfRounds = 20)
    roundManager = roundManager.copy(currentRound = 1)
    roundManager = roundManager.cardDistribution()
    roundManager = roundManager.copy(predictionPerRound = List(0, 1, 2))

    val xml = roundManager.toXML

    val rM2 = RoundManager.fromXML(xml, roundManager)
    println(rM2 == roundManager)*/
  }
}
