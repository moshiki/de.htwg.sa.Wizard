package de.htwg.se.wizard

import com.google.inject.AbstractModule
import de.htwg.sa.wizard.controller.controllerComponent.ResultTableControllerInterface
import de.htwg.sa.wizard.controller.controllerComponent.controllerBaseImplementation.ResultTableController
import de.htwg.sa.wizard.model.resultTableComponent.ResultTableInterface
import de.htwg.sa.wizard.model.resultTableComponent.resultTableBaseImplementation.ResultTable
import de.htwg.se.wizard.model.fileIOComponent.{FileIOInterface, _}
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import net.codingwell.scalaguice.ScalaModule

class WizardModule extends AbstractModule with ScalaModule{
  override def configure(): Unit = {
    bind[ModelInterface].toInstance(RoundManager())
    bind[FileIOInterface].to[FileIOJSON.FileIO]
    bind[ResultTableControllerInterface].to[ResultTableController]
    bind[ResultTableInterface].toInstance(ResultTable())
    bind[de.htwg.sa.wizard.model.fileIOComponent.FileIOInterface].to[de.htwg.sa.wizard.model.fileIOComponent.FileIOJSON.FileIO]
  }
}
