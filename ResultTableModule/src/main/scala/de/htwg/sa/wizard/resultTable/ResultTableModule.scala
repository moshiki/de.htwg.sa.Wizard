package de.htwg.sa.wizard.resultTable

import com.google.inject.AbstractModule
import de.htwg.sa.wizard.resultTable.model.dbComponent.DaoInterface
import de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentSlickImplementation.DaoSlick
import de.htwg.sa.wizard.resultTable.model.fileIOComponent.FileIOInterface
import de.htwg.sa.wizard.resultTable.model.fileIOComponent.FileIOJSON.FileIO
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.resultTableBaseImplementation.ResultTable
import net.codingwell.scalaguice.ScalaModule

class ResultTableModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ResultTableInterface].toInstance(ResultTable())
    bind[FileIOInterface].to[FileIO]
    bind[DaoInterface].to[DaoSlick]
  }
}
