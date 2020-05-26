package de.htwg.sa.wizard.resultTable.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import de.htwg.sa.wizard.resultTable.controller.controllerComponent.ResultTableControllerInterface
import de.htwg.sa.wizard.resultTable.util.ArrayArrayIntContainer
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContextExecutor, Future}

class ResultTableModuleHttpServer(resultTableControllerInterface: ResultTableControllerInterface) {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route = concat(
    get {
      path("resultTable" / "table") {
        complete(resultTableControllerInterface.tableAsString)
      }
    },
    get {
      path("resultTable" / "pointArrayForView") {
        val container = ArrayArrayIntContainer(resultTableControllerInterface.pointArrayForView)
        complete(Json.toJson(container).toString())
      }
    }

    ,get {path("test")(complete("Hello"))}
  )

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 54251)

  def shutdownWebServer() : Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
