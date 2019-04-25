import de.htwg.se.wizard.model.cards.WizardCard

/*object CardStack {
  val initialCardStack = {
    val list2 = Nil
    for {i <-1 to 4} list2:::List(WizardCard())
    list2
  }
}*/

//CardStack.initialCardStack

val list = List.fill(5)(WizardCard())