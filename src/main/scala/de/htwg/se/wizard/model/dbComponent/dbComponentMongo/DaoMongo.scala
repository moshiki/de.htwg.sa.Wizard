package de.htwg.se.wizard.model.dbComponent.dbComponentMongo

import de.htwg.se.wizard.model.dbComponent.DaoInterface
import de.htwg.se.wizard.model.modelComponent.ModelInterface
import org.mongodb.scala.result.InsertOneResult
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observer, SingleObservable}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

case class DaoMongo() extends DaoInterface {
  val uri: String = "mongodb://" + sys.env.getOrElse("MONGODB_HOST", "localhost")
  val client: MongoClient = MongoClient(uri)
  val database: MongoDatabase = client.getDatabase("wizard")
  val wizardCollection: MongoCollection[Document] = database.getCollection("wizard")

  override def load(modelInterface: ModelInterface): Future[(ModelInterface, String)] = Future {
    val resultFuture = wizardCollection.find().first().head()
    val result = Await.result(resultFuture, Duration.Inf)
    val controllerState = (Json.parse(result.getString("wizard")) \ "controllerState").get.as[String]
    val modelInterfaceJson = (Json.parse(result.getString("wizard")) \ "modelState").get
    val newModelInterface = modelInterface.fromJson(modelInterfaceJson)
    (newModelInterface, controllerState)
  }

  override def save(modelInterface: ModelInterface, controllerState: String): Future[Unit] = Future {
      val state = Json.obj(
        "controllerState" -> controllerState,
        "modelState" -> modelInterface.toJson
      )

      val wizardDoc: Document = Document("wizard" -> Json.prettyPrint(state))
      val insertObservable: SingleObservable[InsertOneResult] = wizardCollection.insertOne(wizardDoc)
      insertObservable.subscribe(new Observer[InsertOneResult] {
        override def onNext(result: InsertOneResult): Unit = println(s"inserted: $result")

        override def onError(e: Throwable): Unit = println(s"failed: $e")

        override def onComplete(): Unit = println("completed")
      })
  }
}
