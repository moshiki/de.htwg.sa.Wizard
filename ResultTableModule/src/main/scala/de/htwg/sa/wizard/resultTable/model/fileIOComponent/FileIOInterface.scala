package de.htwg.sa.wizard.resultTable.model.fileIOComponent

import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface

import scala.util.Try

trait FileIOInterface {
  def load(resultTableInterface: ResultTableInterface, path: String): Try[ResultTableInterface]

  def save(resultTableInterface: ResultTableInterface, path: String): Try[Unit]
}
