package de.htwg.se.wizard

import com.google.inject.AbstractModule
import de.htwg.se.wizard.model.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.model.modelComponent.{ResultTable, RoundManager}
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.wizard.model.fileIOComponent._

class WizardModule extends AbstractModule with ScalaModule{
  override def configure(): Unit = {
    bind[RoundManager].toInstance(RoundManager(resultTable = ResultTable.initializeTable()))
    bind[FileIOInterface].to[FileIOXML.FileIO]
  }
}
