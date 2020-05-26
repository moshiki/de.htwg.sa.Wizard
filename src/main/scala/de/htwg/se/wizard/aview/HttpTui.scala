package de.htwg.se.wizard.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.stream.ActorMaterializer
import de.htwg.se.wizard.controller.controllerComponent.ControllerInterface

import scala.concurrent.{ExecutionContextExecutor, Future}

class HttpTui (val controllerInterface: ControllerInterface) {
  // code taken from https://doc.akka.io/docs/akka-http/current/introduction.html (26.05.2020)

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route = {get {
    path("wizard"){
      gameToHtml()
    }~
    path("wizard" / "undo"){
      controllerInterface.undo()
      gameToHtml()
    }~
    path("wizard" / "redo"){
      controllerInterface.redo()
      gameToHtml()
    }~
    path("wizard" / "save"){
      controllerInterface.save()
      gameToHtml()
    }~
    path("wizard" / "load"){
      controllerInterface.load()
      gameToHtml()
    }~
    path("wizard" / Segment){
      input =>
        controllerInterface.eval(input)
        gameToHtml()
    }
  }~
  post {
    path("wizard") {
      decodeRequest {
        entity(as[String]) { input => {
          controllerInterface.eval(input.replace("input=", ""))
          gameToHtml()
        }
        }
      }
    }
  }
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8080)

  def shutdownWebServer() : Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def gameToHtml(): StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Wizard</h1><p>" + controllerInterface.currentStateAsHtml +
    """</p><form action="/wizard" method="post">
      |  <input type="text" id="input" name="input">
      |  <input type="submit" value="Submit">
      |</form> """.stripMargin))
  }
}
