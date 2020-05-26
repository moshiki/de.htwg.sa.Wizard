package de.htwg.sa.wizard.resultTable.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import de.htwg.sa.wizard.resultTable.controller.controllerComponent.ResultTableControllerInterface
import de.htwg.sa.wizard.resultTable.util.{ArrayArrayIntContainer, InitializeTableArgumentContainer, StringListContainer, UpdatePointsArgumentContainer}
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContextExecutor, Future}

class ResultTableModuleHttpServer(resultTableControllerInterface: ResultTableControllerInterface) {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route = concat(
    get {
      path("resultTable" / "safe") {
        resultTableControllerInterface.safe()
        complete("")
      }
    },
    get {
      path("resultTable" / "load") {
        resultTableControllerInterface.load()
        complete("")
      }
    },
    get {
      path("resultTable" / "pointArrayForView") {
        val container = ArrayArrayIntContainer(resultTableControllerInterface.pointArrayForView)
        complete(Json.toJson(container).toString())
      }
    },
    get {
      path("resultTable" / "table") {
        complete(resultTableControllerInterface.tableAsString)
      }
    },
    get {
      path("resultTable" / "playerList") {
        val container = StringListContainer(resultTableControllerInterface.playerList)
        complete(Json.toJson(container).toString())
      }
    },
    put {
      path("resultTable" / "table") {
        decodeRequest {
          entity(as[String]) {string => {
            val container = Json.fromJson(Json.parse(string))(UpdatePointsArgumentContainer.containerReads).get
            resultTableControllerInterface.updatePoints(container.round, container.points)
            complete("")
          }
          }
        }
      }
    },
    post {
      path("resultTable" / "table") {
        decodeRequest {
          entity(as[String]) {string => {
            val container = Json.fromJson(Json.parse(string))(InitializeTableArgumentContainer.containerReads).get
            resultTableControllerInterface.initializeTable(container.numberOfRounds, container.numberOfPlayers)
            complete("")
          }
          }
        }
      }
    }
  )

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 54251)

  def shutdownWebServer() : Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
