package de.htwg.sa.wizard.model.fileIOComponent

import de.htwg.sa.wizard.model.resultTableComponent.ResultTableInterface

import scala.util.Try

trait FileIOInterface {
  def load(resultTableInterface: ResultTableInterface, path: String): Try[ResultTableInterface]

  def save(resultTableInterface: ResultTableInterface, path: String): Try[Unit]
}
