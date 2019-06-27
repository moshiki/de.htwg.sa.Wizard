package de.htwg.se.wizard

import com.google.inject.AbstractModule
import de.htwg.se.wizard.controller.fileIOComponent.FileIOInterface
import de.htwg.se.wizard.controller.maincontroller.RoundManager
import de.htwg.se.wizard.model.modelComponent.cards.StaticCard
import de.htwg.se.wizard.model.modelComponent.{ResultTableBuilder, StaticPlayer}
import de.htwg.se.wizard.model.{ResultTableBuilderInterface, StaticCardInterface, StaticPlayerInterface}
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.wizard.controller.fileIOComponent._

class WizardModule extends AbstractModule with ScalaModule{
  override def configure(): Unit = {
    bind[StaticPlayerInterface].to[StaticPlayer]
    bind[StaticCardInterface].to[StaticCard]
    bind[ResultTableBuilderInterface].to[ResultTableBuilder]
    bind[RoundManager].toInstance(RoundManager(resultTable = ResultTableBuilder().initializeTable(),
      staticPlayerInterface = StaticPlayer(),
      staticCardInterface = StaticCard()))
    bind[FileIOInterface].to[FileIOXML.FileIO]
  }
}
