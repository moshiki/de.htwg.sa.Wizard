package de.htwg.sa.wizard.cardModule

import com.google.inject.{Guice, Injector}
import de.htwg.sa.wizard.cardModule.controller.controllerComponent.CardControllerInterface
import de.htwg.sa.wizard.cardModule.controller.controllerComponent.controllerBaseImplementation.CardController

object CardMod {
  val injector: Injector = Guice.createInjector(new CardModule)
  val controller: CardControllerInterface = injector.getInstance(classOf[CardController])
  @volatile var exitServer = false

  val httpServer: CardModuleHttpServer = CardModuleHttpServer(controller)

  def main(args: Array[String]): Unit = {
    while (!exitServer) {
      Thread.sleep(1000)
    }
    httpServer.shutdownWebServer()
  }
}
