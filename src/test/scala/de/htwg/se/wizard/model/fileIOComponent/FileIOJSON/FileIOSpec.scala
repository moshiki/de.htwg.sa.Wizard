package de.htwg.se.wizard.model.fileIOComponent.FileIOJSON

import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import org.scalatest.{Matchers, WordSpec}

class FileIOSpec extends WordSpec with Matchers{
  "A JSON FileIO" should {
    "save and restore the whole game" in {
      val roundManager = RoundManager.Builder().build()
      val orig = roundManager.copy()
      val controller = new Controller(roundManager)
      val fileIO = FileIO()
      fileIO.save(controller.controllerStateAsString, roundManager)
      val res = fileIO.load(roundManager)
      res._2 == orig should be(true)
    }
  }
}
