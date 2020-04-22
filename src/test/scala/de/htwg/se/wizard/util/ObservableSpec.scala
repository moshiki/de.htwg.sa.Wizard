package de.htwg.se.wizard.util

import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ObservableSpec extends AnyWordSpec with Matchers with MockFactory {
  "An Observable" should {
    val observable = new Observable
    val observer = stub[Observer]
    "add an Observer" in {
      observable.add(observer)
      observable.subscribers should contain(observer)
    }
    "notify an Observer" in {
      observable.notifyObservers()
      (observer.update _).verify().once()
    }
    "remove an Observer" in {
      observable.remove(observer)
      observable.subscribers should not contain observer
    }
  }
}
