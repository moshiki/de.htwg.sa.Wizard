package de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentMongo

import de.htwg.sa.wizard.resultTable.model.dbComponent.DaoInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface
import org.mongodb.scala.result.InsertOneResult
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observer, SingleObservable}
import play.api.libs.json.Json

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class DaoMongo() extends DaoInterface() {
  val client: MongoClient = MongoClient()
  val database: MongoDatabase = client.getDatabase("wizard")
  val resultTableCollection: MongoCollection[Document] = database.getCollection("resultTable")

  override def getLatestGame(resultTableInterface: ResultTableInterface): ResultTableInterface = {

    val resultFuture = resultTableCollection.find().first().head()
    val result = Await.result(resultFuture, Duration.Inf)
    val res = Json.parse(result.getString("resultTable"))
    resultTableInterface.fromJson(res)
  }

  override def saveGame(resultTableInterface: ResultTableInterface): Unit = {
    val resultTableDoc: Document = Document("resultTable" -> Json.prettyPrint(resultTableInterface.toJson))

    val insertObservable: SingleObservable[InsertOneResult] = resultTableCollection.insertOne(resultTableDoc)
    insertObservable.subscribe(new Observer[InsertOneResult] {
      override def onNext(result: InsertOneResult): Unit = println(s"inserted: $result")
      override def onError(e: Throwable): Unit = println(s"failed: $e")
      override def onComplete(): Unit = println("completed")
    })
  }
}
