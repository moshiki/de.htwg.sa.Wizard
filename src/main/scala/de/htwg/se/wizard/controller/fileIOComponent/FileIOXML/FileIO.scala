package de.htwg.se.wizard.controller.fileIOComponent.FileIOXML

import de.htwg.se.wizard.controller.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.controller.maincontroller.{Controller, ControllerState, GameOverState, InGameState, PreSetupState, RoundManager, SetupState}
import de.htwg.se.wizard.model.{ResultTableBuilderInterface, StaticCardInterface, StaticPlayerInterface}

import scala.xml.Elem

case class FileIO() extends FileIOInterface{
  override def load(controller: Controller,
                    staticPlayerInterface: StaticPlayerInterface,
                    staticCardInterface: StaticCardInterface,
                    resultTableBuilderInterface: ResultTableBuilderInterface): (ControllerState, RoundManager) = {
    val saveState = scala.xml.XML.loadFile("WizardSaveGame.xml")
    val controllerStateString = (saveState \ "state").text.trim
    val state = controllerStateString match {
      case "PreSetupState" => PreSetupState(controller, staticPlayerInterface, staticCardInterface, resultTableBuilderInterface)
      case "SetupState" => SetupState(controller)
      case "InGameState" => InGameState(controller)
      case "GameOverState" => GameOverState(controller)
    }

    val newRoundManager = RoundManager.fromXML((saveState \ "RoundManager").head, controller.roundManager)
    (state, newRoundManager)
  }

  override def save(controller: Controller): Unit = {
    def gameToXML: Elem = {
      <Game>
        <state>
          {controller.controllerStateAsString}
        </state>
        {controller.roundManager.toXML}
      </Game>
    }

    import java.io._
    val pw = new PrintWriter(new File("WizardSaveGame.xml" ))
    pw.write(gameToXML.toString())
    pw.close()
  }
}
