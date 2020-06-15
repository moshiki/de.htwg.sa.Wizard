package de.htwg.sa.wizard.cardModule.model.dbComponent.dbComponentMongo

import com.mongodb.client.model.FindOneAndUpdateOptions
import de.htwg.sa.wizard.cardModule.model.cardComponent.{CardInterface, CardStackInterface}
import de.htwg.sa.wizard.cardModule.model.dbComponent.DaoInterface
import org.mongodb.scala.model.FindOneAndUpdateOptions
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observer}
import org.mongodb.scala._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._

import scala.collection.JavaConverters._
import play.api.libs.json.Json

case class DaoMongo() extends DaoInterface {

  val uri: String = "mongodb+srv://wizard:wizard@localhost:27017/wizard?retryWrites=true&w=majority"
  System.setProperty("org.mongodb.async.type", "netty")
  val client: MongoClient = MongoClient()
  val database: MongoDatabase = client.getDatabase("wizard")
  val cardStackCollection: MongoCollection[Document] = database.getCollection("cardStack")
  val countersCollection: MongoCollection[Document] = database.getCollection("counters")

  def getNextCounter: Long = {
    countersCollection.updateOne(equal("_id", "sequenceId"), inc("sequence_value", 1))
    1
  }

  override def load(): CardStackInterface = {
    ???
  }

  override def save(cardStackInterface: CardStackInterface): Unit = {
    val cardStackDoc: Document = Document("_id" -> getNextCounter, "cards" -> Document(Json.prettyPrint(Json.toJson(cardStackInterface.cards))))
    cardStackCollection.insertOne(cardStackDoc)
  }
}