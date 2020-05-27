package de.htwg.sa.wizard.cardModule

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.stream.ActorMaterializer
import de.htwg.sa.wizard.cardModule.controller.controllerComponent.CardControllerInterface
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContextExecutor, Future}

case class CardModuleHttpServer(controller: CardControllerInterface) {
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route = concat(
    get {
      path("cardMod") {
        toHtml("<h1>This is the Wizard CardModule Webserver</h1>")
      }
    },
    get {
      path("cardMod" / "exit") {
        CardMod.exitServer = true
        toHtml("<h3>Shutting down CardModule Webserver... bye!</h3>")
      }
    },
    get {
      path("cardStack" / "shuffleCardStack") {
        complete(Json.toJson(controller.shuffleCardStack()).toString())
      }
    },
    get {
      path("cardStack" / "trumpColor") {
        complete(Json.toJson(controller.trumpColor).toString)
      }
    },
    post {
      path("cardStack" / "cardsForPlayer") {
        decodeRequest {
          entity(as[String]) { string => {
            val params = Json.parse(string)
            complete(controller.cardsForPlayer(params.playerNumber, params.currentRound))
          }
          }
        }
      }
    }
  )

  println(s"CardModule Server online at http://localhost:1234/")

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 1234)

  def shutdownWebServer() : Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def toHtml(html: String): StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
  }
}
