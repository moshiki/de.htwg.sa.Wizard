package de.htwg.se.wizard.controller.fileIOComponent.FileIOXML

import de.htwg.se.wizard.controller.maincontroller.{Controller, RoundManager}
import de.htwg.se.wizard.model.modelComponent.cards.StaticCard
import de.htwg.se.wizard.model.modelComponent.{ResultTableBuilder, StaticPlayer}
import org.scalatest.{Matchers, WordSpec}

class FileIOSpec extends WordSpec with Matchers{
  "An XML File IO" should {
    "save and restore the whole game" in {
      val roundManager = RoundManager.Builder().build(StaticPlayer(), StaticCard(), ResultTableBuilder())
      val controller = new Controller(roundManager, StaticPlayer(), StaticCard(), ResultTableBuilder())
      val fileIO = FileIO()
      fileIO.save(controller)
      val res = fileIO.load(controller, StaticPlayer(), StaticCard(), ResultTableBuilder())
      res._2 should be(roundManager)
    }
  }
}
