package de.htwg.sa.wizard.resultTable.model.dbComponent.dbComponentMongo

import de.htwg.sa.wizard.resultTable.model.dbComponent.DaoInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface
import org.mongodb.scala.result.InsertOneResult
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observable, Observer, SingleObservable}
import play.api.libs.json.{JsBoolean, JsValue, Json}

case class DaoMongo() extends DaoInterface() {
  val client: MongoClient = MongoClient()
  val database: MongoDatabase = client.getDatabase("wizard")
  val resultTableCollection: MongoCollection[Document] = database.getCollection("resultTable")

  override def getLatestGame(resultTableInterface: ResultTableInterface): ResultTableInterface = {
    var waitOnRes = true
    var res: JsValue = JsBoolean(true)
    val observable: Observable[Document] = resultTableCollection.find().first()

    observable.subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = {
        res = Json.parse(result("resultTable").toString)
      }

      override def onError(e: Throwable): Unit = println("failed to load data")

      override def onComplete(): Unit = {
        waitOnRes = false
        println("completed loading data")
      }
    })

    while(waitOnRes)
      Thread.sleep(10)

    println(res)
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
