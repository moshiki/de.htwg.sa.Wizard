package de.htwg.sa.wizard.cardModule.model.dbComponent.dbComponentMongo

import de.htwg.sa.wizard.cardModule.model.cardComponent.{CardInterface, CardStackInterface}
import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.CardStack
import de.htwg.sa.wizard.cardModule.model.dbComponent.DaoInterface
import org.mongodb.scala._
import org.mongodb.scala.result.InsertOneResult
import play.api.libs.json._

case class DaoMongo() extends DaoInterface {
  val uri: String = "mongodb://" + sys.env.getOrElse("MONGODB_HOST", "localhost")
  val client: MongoClient = MongoClient(uri)
  val database: MongoDatabase = client.getDatabase("wizard")
  val cardStackCollection: MongoCollection[Document] = database.getCollection("cardStack")


  override def load(): CardStackInterface = {
    var waitOnRes = true
    var res: JsValue = JsBoolean(true)
    val observable: Observable[Document] = cardStackCollection.find().first()

    observable.subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = {
        res = Json.parse(result.getString("cards"))
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
    var cards: List[CardInterface] = Nil
    var cardStack = CardStack()
    cardStack.fromJson(res)
  }

  override def save(cardStackInterface: CardStackInterface): Unit = {
    val cardStackDoc: Document = Document("cards" -> cardStackInterface.toJson.toString())

    val insertObservable: SingleObservable[InsertOneResult] = cardStackCollection.insertOne(cardStackDoc)
    insertObservable.subscribe(new Observer[InsertOneResult] {
      override def onNext(result: InsertOneResult): Unit = println(s"inserted: $result")
      override def onError(e: Throwable): Unit = println(s"failed: $e")
      override def onComplete(): Unit = println("completed")
    })
  }
}