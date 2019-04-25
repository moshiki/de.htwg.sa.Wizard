package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards._

object CardStack {
  val initialize: List[Card] = {
    val wizards = List.fill(4)(WizardCard())
    val jesters = List.fill(4)(JesterCard())


    wizards ::: jesters
  }
}
