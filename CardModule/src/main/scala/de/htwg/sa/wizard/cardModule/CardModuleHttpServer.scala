package de.htwg.sa.wizard.cardModule

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import de.htwg.sa.wizard.cardModule.controller.controllerComponent.CardControllerInterface
import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface
import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface._
import de.htwg.sa.wizard.cardModule.util._

import scala.concurrent.{ExecutionContextExecutor, Future}

case class CardModuleHttpServer(controller: CardControllerInterface) extends PlayJsonSupport {
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
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
        complete(controller.shuffleCardStack())
      }
    },
    get {
      path("cardStack" / "trumpColor") {
        complete(controller.trumpColor)
      }
    },get {
      path("cardStack" / "cardsForPlayer") {
        complete(controller.cardsForPlayer(1, 5))
      }
    },
    post {
      path("cardStack" / "cardsForPlayer") {
            entity(as[CardsForPlayerArgumentContainer]) { params =>
              complete(controller.cardsForPlayer(params.playerNumber, params.currentRound))
            }
      }
    },
    post {
      path("cardStack" / "splitCardStack") {
          entity(as[SplitCardStackArgumentContainer]) { params => {
            controller.splitCardStack(params.numberOfPlayers, params.currentRound)
            complete(StatusCodes.OK)
          }
        }
      }
    },
    post {
      path("cardStack" / "assignCardsToPlayer") {
          entity(as[AssignCardsToPlayerArgumentContainer]) { params => {
            complete(controller.assignCardsForPlayer(params.cards, params.playerName))
          }
        }
      }
    },
    get {
      path("cardStack" / "topOfCardStackString") {
        complete(controller.topOfCardStackString())
      }
    },
    post {
      path("cardStack" / "playerOfHighestCard") {
          entity(as[List[CardInterface]]) { cardList => {
            complete(controller.playerOfHighestCard(cardList))
          }
        }
      }
    }
  )

  println(s"CardModule Server online at http://0.0.0.0:1234/")

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "0.0.0.0", 1234)

  def shutdownWebServer() : Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def toHtml(html: String): StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
  }
}
