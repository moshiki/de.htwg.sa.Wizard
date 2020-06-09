package de.htwg.se.wizard.model.modelComponent

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface

import scala.xml.Elem


case class Player(name: String, playerCards: List[CardInterface] = Nil) {
  override def toString: String = name

  def assignCards(cards: List[CardInterface]): Player = {
    this.copy(playerCards = cards)
  }

  def toXML: Elem = {
    <Player>
      <name>
        {name}
      </name>
      <playerCards>
        {if (playerCards.nonEmpty) playerCards.map(card => card.toXML) else "None"}
      </playerCards>
    </Player>
  }
}

object Player {
  def checkNumberOfPlayers(number: Int): Boolean = number >= 3 && number <= 5

  def playerTurn(player: Player, round: Int): String = {
    val cards = player.playerCards
    s"""Round $round - Player: ${player.name}
       |Select one of the following cards:
       |{ ${cards.mkString(", ")} }""".stripMargin
  }

  def playerPrediction(player: Player, round: Int, trump: Option[String]): String = {
    s"""Round $round - Player: ${player.name}
       |Trump Color: ${trump.getOrElse("None")}
       |Your Cards: { ${player.playerCards.mkString(", ")} }
       |Guess your amount of tricks: """.stripMargin
  }

  def fromXML(node: scala.xml.Node): Player = {
    val name = (node \ "name").text.trim
    val player = Player(name)
    val cards = (node \ "playerCards").head.child.filter(node => node.text.trim != "")
    val playerCards = if (cards.text.trim != "None")  {cards.map(node => CardInterface(node.label).fromXML(node)).toList} else Nil
    player.copy(playerCards = playerCards)
  }

  import play.api.libs.json._

  implicit val playerReads: Reads[Player] = Json.reads[Player]
  implicit val playerWrites: OWrites[Player] = Json.writes[Player]
}