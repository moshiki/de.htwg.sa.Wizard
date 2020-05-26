package de.htwg.sa.wizard.resultTable

import com.google.inject.{Guice, Injector}
import de.htwg.sa.wizard.resultTable.aview.ResultTableModuleHttpServer
import de.htwg.sa.wizard.resultTable.controller.controllerComponent.controllerBaseImplementation.ResultTableController

object ResultTable {
  val injector: Injector = Guice.createInjector(new ResultTableModule)
  val controller: ResultTableController = injector.getInstance(classOf[ResultTableController])

  val httpServer = new ResultTableModuleHttpServer(controller)
  @volatile var shutdown = false

  def main(args: Array[String]): Unit = {
    while(!shutdown) {
      Thread.sleep(1000)
      ;
    }

    httpServer.shutdownWebServer()
  }

  def shutdownServer():Unit = shutdown = true
}
