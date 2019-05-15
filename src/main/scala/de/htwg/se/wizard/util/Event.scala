package de.htwg.se.wizard.util

trait Event

case class PreSetupFinishedEvent()

case class SetupFinishedEvent() extends Event

case class GameOverEvent() extends Event
