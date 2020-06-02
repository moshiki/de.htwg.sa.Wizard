package de.htwg.se.wizard

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer
import com.google.inject.{Guice, Injector}

import de.htwg.sa.wizard.resultTable.ResultTable
import de.htwg.se.wizard.aview.gui.SwingGui
import de.htwg.se.wizard.aview.{HttpTui, TUI}
import de.htwg.se.wizard.controller.controllerComponent.controllerBaseImpl.Controller

import scala.io.StdIn.readLine

object Wizard {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val injector: Injector = Guice.createInjector(new WizardModule)
  val controller: Controller = injector.getInstance(classOf[Controller])
  val dockerenv: String = sys.env.getOrElse("DOCKERENV", "FALSE")

  val tui = new TUI(controller)
  if (dockerenv == "FALSE") {
    val gui = new SwingGui(controller)
  }
  val httpTui = new HttpTui(controller)

  ResultTable.main(Array())
  controller.notifyObservers()

  def main(args: Array[String]): Unit = {
    var input: String = ""
    do {
      input = readLine()
      tui.processInput(input)
    } while (input != "q")
    httpTui.shutdownWebServer()
    Http().singleRequest(HttpRequest(uri = "http://localhost:1234/cardMod/exit"))
  }
}
