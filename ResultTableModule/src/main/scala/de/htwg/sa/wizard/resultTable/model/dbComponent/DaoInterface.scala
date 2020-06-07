package de.htwg.sa.wizard.resultTable.model.dbComponent

import de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentSlickImplementation.ResultTableTable
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface

trait DaoInterface {
  def getLatestGame: ResultTableInterface

  def saveGame(daoResultTable: ResultTableTable): Unit
}
