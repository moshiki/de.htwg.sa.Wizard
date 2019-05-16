package de.htwg.se.wizard.model.cards

import scala.util.Random

object CardStack {
  val initialize: List[Card] = {
    val wizards = List.fill(4)(Card.apply("wizard"))
    val jesters = List.fill(4)(Card.apply("jester"))
    val normals = for {
      color <- List("red", "blue", "yellow", "green")
      number <- 1 to 13
    } yield DefaultCard(color, number)

    wizards ::: jesters ::: normals
  }

  def shuffleCards(a:List[Card]): List[Card] = {
    Random.shuffle(a)
  }
}
