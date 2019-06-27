package de.htwg.se.wizard

import de.htwg.se.wizard.model.modelComponent.cards.{JesterCard, StaticCard, WizardCard}
import de.htwg.se.wizard.model.modelComponent.{Player, StaticPlayer}

object xmltest {
  def main(args: Array[String]): Unit = {
    /*val player = Player("P1")//, playerCards = Some(List(WizardCard(), JesterCard())))
    val playerXML = player.toXML

    println(playerXML.toString())
    println()

    val newPlayer = StaticPlayer().fromXML(playerXML)
    println(newPlayer)*/

    val card = WizardCard()//owner = Some(Player("P1")))
    val xml = card.toXML
    //println(xml)
    val card2 = StaticCard().fromXML(xml)
    println(card2)
  }
}
