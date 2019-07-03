package de.htwg.se.wizard.model.modelComponent.modelBaseImpl

import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards.Card

import scala.xml.Elem


case class Player(name: String, playerCards: Option[List[Card]] = None) {
  override def toString: String = name

  def getPlayerCards: Option[List[Card]] = {
    playerCards
  }

   def assignCards(cards: Option[List[Card]]): Player = {
    this.copy(playerCards = cards)
  }

   def getName: String = {
    name
  }

   def toXML: Elem = {
    <Player>
      <name>
        {name}
      </name>
      <playerCards>
        {if (playerCards.isDefined) playerCards.get.map(card => card.toXML)
      else "None"}
      </playerCards>
    </Player>
  }
}

object Player {
  def checkNumberOfPlayers(number: Int): Boolean = {
    if (number < 3 || number > 5) return false
    true
  }


  def playerTurn(player: Player, round: Int): String = {

    val cards = player.getPlayerCards.get


    val firstString = "Round " + round + " - Player: " + player.getName
    val secondString = "Select one of the following cards:"

    firstString + "\n" + secondString + "\n" + "{ " + cards.mkString(", ") + " }"
  }

  def playerPrediction(player: Player, round: Int, trump: Option[String]): String = {
    val firstString = "Round " + round + " - Player: " + player.getName
    val secondString = "Trump Color: " + trump.getOrElse("None")
    val thirdString = "Your Cards: " + "{ " + player.getPlayerCards.get.mkString(", ") + " }"
    val string = "Enter the amount of stitches you think you will get: "
    firstString + "\n" + secondString + "\n" + thirdString + "\n" + string
  }

  def fromXML(node: scala.xml.Node): Player = {
    val name = (node \ "name").text.trim
    val player = Player(name)

    val cards = (node \ "playerCards").head.child.filter(node => node.text.trim != "")

    var playerCards: Option[List[Card]] = None
    if (cards.text.trim != "None")  {
      val playerCardsList = cards.map(node => Card.fromXML(node)).toList//.map(card => StaticCard().setOwner(card, player))
      playerCards = Some(playerCardsList)
    }

    player.copy(playerCards = playerCards)
  }

  import play.api.libs.json._
  //implicit val playerReads: Reads[Player] = Json.reads[Player]
  implicit val playerWrites: OWrites[Player] = Json.writes[Player]
}