package de.htwg.se.wizard.aview.gui

import de.htwg.se.wizard.controller.controllerComponent.ControllerInterface
import javax.swing.{BorderFactory, ImageIcon, JOptionPane}

import scala.collection.immutable
import scala.swing.Swing._
import scala.swing._
import scala.swing.event.{ButtonClicked, Key, KeyPressed, MouseClicked}

class InGamePanel(controller: ControllerInterface) extends BoxPanel(Orientation.Vertical) {
  val currentPlayer: String = controller.currentPlayerString

  if (controller.predictionMode) preferredSize = new Dimension(1000, 760)
  else preferredSize = new Dimension(1000, 880)
  border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
  val myFont = new Font("Herculanum", java.awt.Font.PLAIN, 20)

  contents += new BoxPanel(Orientation.Horizontal) {
    if (!controller.predictionMode) {
      contents += new Label("Player: " + currentPlayer + " - Tricks: " +
        controller.currentAmountOfTricks + " (Prediction: " +
        controller.playerPrediction + ")") {
        font = myFont
      }
    } else {
      contents += new Label("Player: " + currentPlayer) {
        font = myFont
      }
    }
    contents += HGlue
    contents += new Label("Round " + controller.currentRound) {
      font = myFont
    }
  }

  contents += RigidBox(new Dimension(0, 20))

  contents += new BoxPanel(Orientation.Horizontal) {
    if (!controller.predictionMode) {
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

    if (!controller.predictionMode) {
      contents += new ScrollPane() {
        // This pane shows all played cards
        contents = new FlowPanel() {
          val playedCards: List[String] = controller.playedCardsAsString

          val labelList: immutable.IndexedSeq[Label] = for (i <- playedCards.indices) yield new Label {
            private val temp = new ImageIcon("src/main/resources/cards/" + playedCards(i) + ".png").getImage
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
          val playerCards: List[String] = controller.currentPlayersCards
          val labelList: immutable.IndexedSeq[Label] = for (i <- playerCards.indices) yield new Label {
            private val temp = new ImageIcon("src/main/resources/cards/" + playerCards(i) + ".png").getImage
            private val resize = temp.getScaledInstance(100, 133, java.awt.Image.SCALE_SMOOTH)
            icon = new ImageIcon(resize)
          }

          labelList.foreach(x => contents += x)
        }
      }
    }

    contents += HGlue

    contents += new Label {
      private val temp = new ImageIcon("src/main/resources/cards/" + controller.topOfStackCardString + ".png").getImage
      private val resize = temp.getScaledInstance(100, 133, java.awt.Image.SCALE_SMOOTH)
      icon = new ImageIcon(resize)
    }
  }

  if (controller.predictionMode) {
    contents += new FlowPanel(FlowPanel.Alignment.Left)(new Label("Enter your prediction:") {
      font = myFont
    })

    contents += new BoxPanel(Orientation.Horizontal) {
      val predictionTextBox: TextField = new TextField() {
        listenTo(keys)
        reactions += {
          case KeyPressed(_, Key.Enter, _, _) => controller.eval(text)
        }
      }
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
        val playerCards: List[String] = controller.currentPlayersCards
        val labelList: immutable.IndexedSeq[Label] = for (i <- playerCards.indices) yield new Label {
          val index: Int = i
          private val temp = new ImageIcon("src/main/resources/cards/" + playerCards(i) + ".png").getImage
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
      contents = new Table(controller.resultArray, controller.playersAsStringList)
    }
  }

  contents += new FlowPanel() {
    val saveButton: Button = new Button("Save current progress...") {
      font = myFont
    }

    contents += saveButton

    listenTo(saveButton)

    reactions += {
      case ButtonClicked(`saveButton`) =>
        controller.save()
        JOptionPane.showMessageDialog(null, "Saving was successful.")
    }
  }
}
