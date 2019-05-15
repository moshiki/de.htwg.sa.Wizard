package de.htwg.se.wizard.util

trait ControllerUpdateStateObserver {
  def update(): Unit
}

class ControllerUpdateStateObservable {
  var subscribers: Vector[ControllerUpdateStateObserver] = Vector()

  def add(s: ControllerUpdateStateObserver): Unit = subscribers = subscribers :+ s

  def remove(s: ControllerUpdateStateObserver): Unit = subscribers = subscribers.filterNot(o => o == s)

  def triggerNextState(): Unit = subscribers.foreach(o => o.update())
}
