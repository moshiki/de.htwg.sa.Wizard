package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.{Controller, RoundManager}
import de.htwg.se.wizard.model.Player
import de.htwg.se.wizard.model.cards.Card
import javax.swing.ImageIcon
import javax.swing.table.AbstractTableModel

import scala.swing._
import Swing._
import scala.collection.immutable
import scala.swing.event.{ButtonClicked, MouseClicked}

class InGamePanel(controller: Controller) extends BoxPanel(Orientation.Vertical) {
  val roundManager: RoundManager = controller.roundManager
  val currentPlayer: Player = roundManager.players(roundManager.currentPlayer)

  contents += new BoxPanel(Orientation.Horizontal) {
    contents += new Label("Player: " + currentPlayer)
    contents += HGlue
    contents += new Label("Round " + roundManager.currentRound)
  }

  contents += new BoxPanel(Orientation.Horizontal) {
    contents += new GridPanel(2, 2) {

      if (!roundManager.predictionMode) {
        contents += new Label("Already played cards:")
      } else {
        contents += new Label("Your cards:")
      }

      contents += new Label("Trump Color:")

      if (!roundManager.predictionMode) {
        contents += new ScrollPane() {
          // This pane shows all played cards
          contents = new FlowPanel() {
            val playedCards: List[Card] = roundManager.playedCards

            val labelList: immutable.IndexedSeq[Label] = for (i <- playedCards.indices) yield new Label {
              private val temp = new ImageIcon("src/main/resources/" + playedCards(i) + ".png").getImage
              private val resize = temp.getScaledInstance(150, 200, java.awt.Image.SCALE_SMOOTH)
              icon = new ImageIcon(resize)
            }

            labelList.foreach(x => contents += x)
          }
        }
      } else {
        contents += new ScrollPane() {
          // This pane shows all cards of the current player
          contents = new FlowPanel() {
            val playerCards: List[Card] = currentPlayer.playerCards.get
            val labelList: immutable.IndexedSeq[Label] = for (i <- playerCards.indices) yield new Label {
              val index: Int = i
              private val temp = new ImageIcon("src/main/resources/" + playerCards(i) + ".png").getImage
              private val resize = temp.getScaledInstance(150, 200, java.awt.Image.SCALE_SMOOTH)
              icon = new ImageIcon(resize)
              /*listenTo(mouse.clicks)
            reactions += {
              case _: MouseClicked => println(playerCards(index))
            }*/
            }

            labelList.foreach(x => contents += x)
          }
        }
      }

      contents += new Label {
        private val temp = new ImageIcon("src/main/resources/" + controller.roundManager.shuffledCardStack.head + ".png").getImage
        private val resize = temp.getScaledInstance(150, 200, java.awt.Image.SCALE_SMOOTH)
        icon = new ImageIcon(resize)
      }
    }

    contents += new ScrollPane {
      contents = new Table(roundManager.resultTable.toAnyArray, roundManager.players)
    }
  }

  if (roundManager.predictionMode) {
    contents += new Label("Enter the amount of stitches you think you will get:")
    contents += new BoxPanel(Orientation.Horizontal) {
      val predictionTextBox = new TextField()
      val nextButton = new Button("->")

      contents += predictionTextBox
      contents += nextButton

      listenTo(nextButton)

      reactions += {
        case ButtonClicked(`nextButton`) => controller.eval(predictionTextBox.text)
      }
    }
  } else {
    contents += new ScrollPane() {
      // This pane shows all cards of the current player
      contents = new FlowPanel() {
        val playerCards: List[Card] = currentPlayer.playerCards.get
        val labelList: immutable.IndexedSeq[Label] = for (i <- playerCards.indices) yield new Label {
          val index:Int = i
          private val temp = new ImageIcon("src/main/resources/" + playerCards(i) + ".png").getImage
          private val resize = temp.getScaledInstance(150, 200, java.awt.Image.SCALE_SMOOTH)
          icon = new ImageIcon(resize)
          listenTo(mouse.clicks)
          reactions += {
            case _: MouseClicked => controller.eval((index + 1).toString)
          }
        }

        labelList.foreach(x => contents += x)
      }
    }
  }

}

// TODO: Font: Herculanum
