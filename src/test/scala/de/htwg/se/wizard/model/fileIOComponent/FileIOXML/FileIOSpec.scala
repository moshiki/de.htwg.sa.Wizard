package de.htwg.se.wizard.model.fileIOComponent.FileIOXML

import de.htwg.se.wizard.controller.maincontroller.{Controller, GameOverState, InGameState, PreSetupState, RoundManager, SetupState}
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
    "save and restore the whole game with all four possible controller states" in {
      val roundManager = RoundManager.Builder().build(StaticPlayer(), StaticCard(), ResultTableBuilder())
      val controller = new Controller(roundManager, StaticPlayer(), StaticCard(), ResultTableBuilder())
      var state = controller.state
      val fileIO = FileIO()
      fileIO.save(controller)
      val res = fileIO.load(controller, StaticPlayer(), StaticCard(), ResultTableBuilder())
      res._1 should be(state)

      state = SetupState(controller)
      controller.state = state
      fileIO.save(controller)
      val res2 = fileIO.load(controller, StaticPlayer(), StaticCard(), ResultTableBuilder())
      res2._1 should be(state)

      state = InGameState(controller)
      controller.state = state
      fileIO.save(controller)
      val res3 = fileIO.load(controller, StaticPlayer(), StaticCard(), ResultTableBuilder())
      res3._1 should be(state)

      state = GameOverState(controller)
      controller.state = state
      fileIO.save(controller)
      val res4 = fileIO.load(controller, StaticPlayer(), StaticCard(), ResultTableBuilder())
      res4._1 should be(state)
    }
  }
}
