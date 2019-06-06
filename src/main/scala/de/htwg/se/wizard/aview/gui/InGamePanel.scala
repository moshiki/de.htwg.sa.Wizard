package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.{Controller, RoundManager}
import de.htwg.se.wizard.model.Player
import de.htwg.se.wizard.model.cards.Card
import javax.swing.{BorderFactory, ImageIcon}

import scala.swing._
import Swing._
import scala.collection.immutable
import scala.swing.event.{ButtonClicked, MouseClicked}

class InGamePanel(controller: Controller) extends BoxPanel(Orientation.Vertical) {
  val roundManager: RoundManager = controller.roundManager
  val currentPlayer: Player = roundManager.players(roundManager.currentPlayer)

  if (roundManager.predictionMode) preferredSize = new Dimension(1000, 720)
  else preferredSize = new Dimension(1000, 840)
  border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
  val myFont = new Font("Herculanum", java.awt.Font.PLAIN, 20)


  contents += new BoxPanel(Orientation.Horizontal) {
    if (!roundManager.predictionMode) {
      contents += new Label("Player: " + currentPlayer + " - Stitches: " +
        roundManager.stitchesPerRound(currentPlayer.toString) + " (Prediction: " +
        roundManager.predictionPerRound(roundManager.currentPlayer) + ")") {
        font = myFont
      }
    } else {
      contents += new Label("Player: " + currentPlayer) {
        font = myFont
      }
    }

    contents += HGlue
    contents += new Label("Round " + roundManager.currentRound) {
      font = myFont
    }
  }

  contents += RigidBox(new Dimension(0, 20))

  contents += new BoxPanel(Orientation.Horizontal) {
    if (!roundManager.predictionMode) {
      contents += new Label("Already played cards") {
        font = myFont
      }
    } else {
      contents += new Label("Your cards") {
        font = myFont
      }
    }

    contents += HGlue

    contents += new Label("Trump Color") {
      font = myFont
    }
  }

  contents += new BoxPanel(Orientation.Horizontal) {

    if (!roundManager.predictionMode) {
      contents += new ScrollPane() {
        // This pane shows all played cards
        contents = new FlowPanel() {
          val playedCards: List[Card] = roundManager.playedCards

          val labelList: immutable.IndexedSeq[Label] = for (i <- playedCards.indices) yield new Label {
            private val temp = new ImageIcon("src/main/resources/" + playedCards(i) + ".png").getImage
            private val resize = temp.getScaledInstance(100, 133, java.awt.Image.SCALE_SMOOTH)
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
            private val resize = temp.getScaledInstance(100, 133, java.awt.Image.SCALE_SMOOTH)
            icon = new ImageIcon(resize)
          }

          labelList.foreach(x => contents += x)
        }
      }
    }

    contents += HGlue

    contents += new Label {
      private val temp = new ImageIcon("src/main/resources/" + controller.roundManager.shuffledCardStack.head + ".png").getImage
      private val resize = temp.getScaledInstance(100, 133, java.awt.Image.SCALE_SMOOTH)
      icon = new ImageIcon(resize)
    }
  }

  if (roundManager.predictionMode) {
    contents += new FlowPanel(FlowPanel.Alignment.Left)(new Label("Enter your prediction:") {
      font = myFont
    })

    contents += new BoxPanel(Orientation.Horizontal) {
      val predictionTextBox = new TextField()
      val nextButton: Button = new Button("Save prediction") {
        font = myFont
      }

      contents += predictionTextBox
      contents += nextButton

      listenTo(nextButton)

      reactions += {
        case ButtonClicked(`nextButton`) => controller.eval(predictionTextBox.text)
      }
    }
  } else {
    contents += new FlowPanel(FlowPanel.Alignment.Left)(new Label("Play one card:") {
      font = myFont
    })
    contents += new ScrollPane() {
      // This pane shows all cards of the current player
      contents = new FlowPanel() {
        val playerCards: List[Card] = currentPlayer.playerCards.get
        val labelList: immutable.IndexedSeq[Label] = for (i <- playerCards.indices) yield new Label {
          val index: Int = i
          private val temp = new ImageIcon("src/main/resources/" + playerCards(i) + ".png").getImage
          private val resize = temp.getScaledInstance(100, 133, java.awt.Image.SCALE_SMOOTH)
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


  contents += new FlowPanel() {
    contents += new ScrollPane {
      contents = new Table(roundManager.resultTable.toAnyArray, roundManager.players)
    }
  }
}
