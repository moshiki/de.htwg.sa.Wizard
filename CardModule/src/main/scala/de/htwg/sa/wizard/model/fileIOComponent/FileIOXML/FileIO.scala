package de.htwg.sa.wizard.model.fileIOComponent.FileIOXML

import java.io.{File, PrintWriter}

import de.htwg.sa.wizard.model.cardComponent.CardInterface
import de.htwg.sa.wizard.model.fileIOComponent.FileIOInterface

import scala.util.Try

case class FileIO() extends FileIOInterface {
  override def load(cardInterface: CardInterface, path: String): Try[CardInterface] = {
    Try {
      val saveData = scala.xml.XML.loadFile(path + ".xml")
      CardInterface.fromXML(saveData)
    }
  }

  override def save(resultTableInterface: CardInterface, path: String): Try[Unit] = {
    Try {
      val pw = new PrintWriter(new File(path + ".xml"))
      pw.write(resultTableInterface.toXML.toString)
      pw.close()
    }
  }
}
