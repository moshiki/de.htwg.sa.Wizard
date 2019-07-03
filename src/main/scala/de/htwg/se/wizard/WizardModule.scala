package de.htwg.se.wizard

import com.google.inject.AbstractModule
import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.wizard.model.fileIOComponent._
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.{ResultTable, RoundManager}

class WizardModule extends AbstractModule with ScalaModule{
  override def configure(): Unit = {
    bind[ModelInterface].toInstance(RoundManager(resultTable = ResultTable.initializeTable()))
    bind[FileIOInterface].to[FileIOJSON.FileIO]
  }
}
