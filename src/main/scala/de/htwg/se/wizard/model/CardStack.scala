package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards._

object CardStack {
  val initialize: List[Card] = {
    val wizards = List.fill(4)(WizardCard())
    val jesters = List.fill(4)(JesterCard())
    val normals = for {
      color <- List("red", "blue", "yellow", "green")
      number <- 1 to 13
    } yield DefaultCard(color, number)

    wizards ::: jesters ::: normals
  }
}
