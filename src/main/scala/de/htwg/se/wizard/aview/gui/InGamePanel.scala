package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.{Controller, RoundManager}
import de.htwg.se.wizard.model.Player
import de.htwg.se.wizard.model.cards.Card
import javax.swing.ImageIcon

import scala.swing._
import Swing._
import scala.collection.immutable
import scala.collection.mutable.ListBuffer
import scala.swing.event.{ButtonClicked, MouseClicked}

class InGamePanel(controller: Controller) extends BoxPanel(Orientation.Vertical) {
  val roundManager:RoundManager = controller.roundManager
  contents += new BoxPanel(Orientation.Horizontal) {
    val currentPlayer:Player = roundManager.players(roundManager.currentPlayer)
    contents += new Label("Round " + roundManager.currentRound)
    contents += HGlue
    contents += new Label("Player: " + currentPlayer)
  }

  val playedCards: List[Card] = roundManager.playedCards

  val labelList: immutable.IndexedSeq[Label] = for (i <- playedCards.indices) yield new Label {
    val index:Int = i
    private val temp = new ImageIcon("src/main/resources/cards/test.png").getImage
    private val resize = temp.getScaledInstance(150, 200, java.awt.Image.SCALE_SMOOTH)
    icon = new ImageIcon(resize)
    listenTo(mouse.clicks)
    reactions += {
      case _: MouseClicked => println("Click 2 + " + index)
    }
  }

  labelList.foreach(x => contents += x)

}

// TODO: Font: Herculanum
