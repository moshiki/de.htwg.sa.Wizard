import de.htwg.se.wizard.model.Player

val player = Player("TestPlayer")
player

case class Card(color: String, number: Integer, owner: Player) {
  def hasColor: Boolean = color != null || color == "none"
  def isWizard: Boolean = number > 13
  def isJester: Boolean = number < 1
  def hasOwner: Boolean = owner != null
}

val card = Card("blue", 13, player)
card.color
card.number
card.owner

card.hasColor
card.isJester
card.isWizard
card.hasOwner

val wizardCard = Card(null, 14, null)
wizardCard.color
wizardCard.number
wizardCard.owner

wizardCard.hasColor
wizardCard.isJester
wizardCard.isWizard
wizardCard.hasOwner

val jesterCard = Card(null, 0, null)
jesterCard.color
jesterCard.number
jesterCard.owner

jesterCard.hasColor
jesterCard.isJester
jesterCard.isWizard
jesterCard.hasOwner

val copyCard = card.copy("green")
copyCard.color
copyCard.number
copyCard.owner

copyCard.hasColor
copyCard.isJester
copyCard.isWizard
copyCard.hasOwner