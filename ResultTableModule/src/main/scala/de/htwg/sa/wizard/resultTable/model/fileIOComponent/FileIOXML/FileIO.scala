package de.htwg.sa.wizard.resultTable.model.fileIOComponent.FileIOXML

import java.io.{File, PrintWriter}

import de.htwg.sa.wizard.resultTable.model.fileIOComponent.FileIOInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface

import scala.util.Try

case class FileIO() extends FileIOInterface {
  override def load(resultTableInterface: ResultTableInterface, path: String): Try[ResultTableInterface] = {
    Try {
      val saveData = scala.xml.XML.loadFile(path + ".xml")
      resultTableInterface.fromXML(saveData)
    }
  }

  override def save(resultTableInterface: ResultTableInterface, path: String): Try[Unit] = {
    Try {
      val pw = new PrintWriter(new File(path + ".xml"))
      pw.write(resultTableInterface.toXML.toString)
      pw.close()
    }
  }
}
