package de.htwg.se.wizard

import com.google.inject.AbstractModule
import de.htwg.sa.wizard.resultTable.controller.controllerComponent.ResultTableControllerInterface
import de.htwg.sa.wizard.resultTable.controller.controllerComponent.controllerBaseImplementation.ResultTableController
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.resultTableBaseImplementation.ResultTable
import de.htwg.se.wizard.model.fileIOComponent._
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import net.codingwell.scalaguice.ScalaModule

class WizardModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ModelInterface].toInstance(RoundManager())
    bind[FileIOInterface].to[FileIOJSON.FileIO]
    bind[ResultTableControllerInterface].to[ResultTableController]
    bind[ResultTableInterface].toInstance(ResultTable())
    bind[de.htwg.sa.wizard.cardModule.model.fileIOComponent.FileIOInterface].to[de.htwg.sa.wizard.cardModule.model.fileIOComponent.FileIOJSON.FileIO]
    bind[de.htwg.sa.wizard.resultTable.model.fileIOComponent.FileIOInterface].to[de.htwg.sa.wizard.resultTable.model.fileIOComponent.FileIOJSON.FileIO]
  }
}
