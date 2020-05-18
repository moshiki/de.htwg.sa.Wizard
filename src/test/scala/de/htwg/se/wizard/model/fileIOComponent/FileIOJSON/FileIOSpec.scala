package de.htwg.se.wizard.model.fileIOComponent.FileIOJSON

import de.htwg.sa.wizard.model.resultTableComponent.ResultTableInterface
import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FileIOSpec extends AnyWordSpec with Matchers with MockFactory{
  "A JSON FileIO" should {
    "save and restore the whole game" in {
      val resultTableStub = stub[ResultTableInterface]
      val fileIOStub = stub[FileIO]
      val roundManager = RoundManager.Builder().build()
      val orig = roundManager.copy()
      val controller = new Controller(roundManager, fileIOStub, resultTableStub)
      fileIOStub.save(controller.controllerStateAsString, roundManager)
      val res = fileIOStub.load(roundManager)
      res.get._1 should be("PreSetupState")
      res.get._2 == orig should be(true)
    }
  }
}
