package de.htwg.se.wizard.controller

import de.htwg.se.wizard.model.RoundManager
import de.htwg.se.wizard.util.Observer
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers{
  "A Controller" when {
    val roundManager = new RoundManager
    val controller = new Controller(roundManager)
    val observer = new Observer {
      var updated: Boolean = false
      def isUpdated: Boolean = updated
      override def update(): Unit = updated = true
    }
    controller.add(observer)
    "observed by an Observer" should {
      "should return the welcome message after initialisation" in {
        controller.getCurrentState should be("Welcome to Wizard!\nPlease enter the number of Players[3-5]:")
      }
      "notify its Observer after evaluating an input string" in {
        controller.eval("4")
        observer.updated should be(true)
        controller.roundManager.numberOfPlayers should be(4)
      }
    }
  }
}
