package de.htwg.se.wizard

import com.google.inject.AbstractModule
import de.htwg.se.wizard.model.dbComponent.DaoInterface
import de.htwg.se.wizard.model.dbComponent.dbComponentMongo.DaoMongo
import de.htwg.se.wizard.model.fileIOComponent._
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import net.codingwell.scalaguice.ScalaModule

class WizardModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ModelInterface].toInstance(RoundManager())
    bind[FileIOInterface].to[FileIOJSON.FileIO]
    bind[DaoInterface].to[DaoMongo]
  }
}
