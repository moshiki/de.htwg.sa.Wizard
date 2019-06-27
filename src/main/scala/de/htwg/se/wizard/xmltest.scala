package de.htwg.se.wizard

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
  }
}
