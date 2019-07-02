package de.htwg.se.wizard.model.fileIOComponent.FileIOXML

import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.RoundManager

import scala.xml.Elem

case class FileIO() extends FileIOInterface{
  override def load(roundManager: RoundManager): (String, RoundManager) = {
    val saveState = scala.xml.XML.loadFile("WizardSaveGame.xml")
    val controllerStateString = (saveState \ "state").text.trim
    val state = controllerStateString

    val newRoundManager = RoundManager.fromXML((saveState \ "RoundManager").head, roundManager)
    (state, newRoundManager)
  }

  override def save(controllerState: String, roundManager: RoundManager): Unit = {
    def gameToXML: Elem = {
      <Game>
        <state>
          {controllerState}
        </state>
        {roundManager.toXML}
      </Game>
    }

    import java.io._
    val pw = new PrintWriter(new File("WizardSaveGame.xml" ))
    pw.write(gameToXML.toString())
    pw.close()
  }
}
