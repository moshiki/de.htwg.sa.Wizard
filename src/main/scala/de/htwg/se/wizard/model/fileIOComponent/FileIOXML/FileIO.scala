package de.htwg.se.wizard.model.fileIOComponent.FileIOXML

import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface

import scala.util.Try
import scala.xml.Elem

case class FileIO() extends FileIOInterface {
  override def load(modelInterface: ModelInterface): Try[(String, ModelInterface)] = {
    Try {
      val saveState = scala.xml.XML.loadFile("WizardSaveGame.xml")
      val controllerStateString = (saveState \ "state").text.trim
      val state = controllerStateString

      val newRoundManager = modelInterface.fromXML((saveState \ "RoundManager").head)
      (state, newRoundManager)
    }
  }

  override def save(controllerState: String, modelInterface: ModelInterface): Try[Unit] = {
    def gameToXML: Elem = {
      <Game>
        <state>
          {controllerState}
        </state>{modelInterface.toXML}
      </Game>
    }

    import java.io._
    Try {
      val pw = new PrintWriter(new File("WizardSaveGame.xml"))
      pw.write(gameToXML.toString())
      pw.close()
    }
  }
}
