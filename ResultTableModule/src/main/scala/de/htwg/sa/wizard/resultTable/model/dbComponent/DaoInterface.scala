package de.htwg.sa.wizard.resultTable.model.dbComponent

import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface

trait DaoInterface {
  def getLatestGame(resultTableInterface: ResultTableInterface): ResultTableInterface

  def saveGame(resultTableInterface: ResultTableInterface): Unit
}
