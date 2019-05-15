package de.htwg.se.wizard.util

import org.scalatest.{Matchers, WordSpec}

class ControllerUpdateStateObserverSpec extends WordSpec with Matchers {
  "A ControllerUpdateStateObserver" should {
    val controllerUpdateStateObservable = new ControllerUpdateStateObservable
    val controllerUpdateStateObserver = new ControllerUpdateStateObserver { // wontfix
      var updated: Boolean = false

      def isUpdated: Boolean = updated

      override def switchToNextState(): Unit = updated = true
    }
    "add an ControllerUpdateStateObserver" in {
      controllerUpdateStateObservable.add(controllerUpdateStateObserver)
      controllerUpdateStateObservable.subscribers should contain(controllerUpdateStateObserver)
    }
    "trigger an ControllerUpdateStateObserver" in {
      controllerUpdateStateObserver.isUpdated should be(false)
      controllerUpdateStateObservable.triggerNextState()
      controllerUpdateStateObserver.isUpdated should be(true)
    }
    "remove an ControllerUpdateStateObserver" in {
      controllerUpdateStateObservable.remove(controllerUpdateStateObserver)
      controllerUpdateStateObservable.subscribers should not contain controllerUpdateStateObserver
    }
  }

}
