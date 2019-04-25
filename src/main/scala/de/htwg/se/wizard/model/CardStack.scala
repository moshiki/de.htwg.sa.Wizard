package de.htwg.se.wizard.model

import de.htwg.se.wizard.model.cards._

object CardStack {
  val initialize: List[Card] = List.fill(4)(WizardCard())
}
