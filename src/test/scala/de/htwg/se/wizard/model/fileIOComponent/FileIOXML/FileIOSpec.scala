package de.htwg.se.wizard.model.fileIOComponent.FileIOXML

import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.wizard.model.dbComponent.DaoInterface
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FileIOSpec extends AnyWordSpec with Matchers with MockFactory {
  "An XML File IO" should {
    "save and restore the whole game" in {
      val roundManager = RoundManager.Builder().withNumberOfPlayers(3).withNumberOfRounds(3).build()
      val fileIO = FileIO()
      val daoInterfaceStub = stub[DaoInterface]
      val controller = new Controller(roundManager, fileIO, daoInterfaceStub)
      fileIO.save(controller.controllerStateAsString, roundManager)
      val res = fileIO.load(roundManager)
      res.get._1 should be("PreSetupState")
      res.get._2 should be(roundManager)
    }
  }
}
