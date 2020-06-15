package de.htwg.sa.wizard.cardModule.model.dbComponent.dbComponentMongo

import de.htwg.sa.wizard.cardModule.model.cardComponent.{CardInterface, CardStackInterface}
import de.htwg.sa.wizard.cardModule.model.dbComponent.DaoInterface
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import play.api.libs.json.Json

case class DaoMongo() extends DaoInterface {

  val uri: String = "mongodb+srv://wizard:wizard@localhost:27017/test?retryWrites=true&w=majority"
  System.setProperty("org.mongodb.async.type", "netty")
  val client: MongoClient = MongoClient()
  val database: MongoDatabase = client.getDatabase("wizard")
  val cardStackCollection: MongoCollection[Document] = database.getCollection("cardStack")

  override def load(): CardStackInterface = {
    ???
  }

  override def save(cardStackInterface: CardStackInterface): Unit = {
    val cardStackDoc: Document = Document(Json.prettyPrint(Json.toJson(cardStackInterface.cards)))
    cardStackCollection.insertOne(cardStackDoc)
  }
}
