package de.htwg.se.wizard.model.fileIOComponent.FileIOXML

import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.RoundManager
import org.scalatest.{Matchers, WordSpec}

class FileIOSpec extends WordSpec with Matchers{
  "An XML File IO" should {
    "save and restore the whole game" in {
      val roundManager = RoundManager.Builder().build()
      val fileIO = FileIO()
      val controller = new Controller(roundManager, fileIO)
      fileIO.save(controller.controllerStateAsString, roundManager)
      val res = fileIO.load(roundManager)
      res._2 should be(roundManager)
    }
  }
}
