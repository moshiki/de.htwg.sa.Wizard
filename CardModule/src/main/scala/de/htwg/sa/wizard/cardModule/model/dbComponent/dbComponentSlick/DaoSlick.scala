package de.htwg.sa.wizard.cardModule.model.dbComponent.dbComponentSlick

import java.sql.Timestamp

import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.CardStack
import de.htwg.sa.wizard.cardModule.model.cardComponent.{CardInterface, CardStackInterface}
import de.htwg.sa.wizard.cardModule.model.dbComponent.DaoInterface
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class DaoSlick() extends DaoInterface {
  val databaseUrl: String = "jdbc:mysql://" + sys.env.getOrElse("DATABASE_HOST", "localhost:3306") + "/" + sys.env.getOrElse("MYSQL_DATABASE", "cardmodule") + "?serverTimezone=UTC"
  val databaseUser: String = sys.env.getOrElse("MYSQL_USER", "wizard")
  val databasePassword: String = sys.env.getOrElse("MYSQL_PASSWORD", "wizard")

  val database = Database.forURL(
    url = databaseUrl,
    driver = "com.mysql.cj.jdbc.Driver",
    user = databaseUser,
    password = databasePassword
  )

  val cardStackTable = TableQuery[CardStackTable]
  val cardTable = TableQuery[CardTable]

  val setup = DBIO.seq((
    cardStackTable.schema
    ++ cardTable.schema
  ).createIfNotExists)
  database.run(setup)

  override def load(): CardStackInterface = {
    val cardStackIdQuery = cardStackTable.sortBy(_.id.desc).take(1).result.head
    val cardStackTuple = Await.result(database.run(cardStackIdQuery), Duration.Inf)
    val cardStackId = cardStackTuple._1

    val cardQuery = cardTable.filter(_.cardStackId === cardStackId).sortBy(_.id.desc).result
    val cardResult = Await.result(database.run(cardQuery), Duration.Inf)
    var cardList: List[CardInterface] = Nil
    cardResult.foreach(card => {cardList = CardInterface.buildCard(card._2, card._3, card._4, card._5) :: cardList})
    CardStack(cardList)
  }

  override def save(cardStackInterface: CardStackInterface): Unit = {
    Await.ready(database.run(cardStackTable += (0, new Timestamp(System.currentTimeMillis()))), Duration.Inf)

    val cardStackIdQuery = cardStackTable.sortBy(_.id.desc).take(1).map(_.id).result.head
    val cardStackId = Await.result(database.run(cardStackIdQuery), Duration.Inf)

    cardStackInterface.cards.foreach(card => {
      Await.ready(database.run(cardTable += (0, card.typeString, card.colorOption, card.owner, card.value, cardStackId)), Duration.Inf)
    })
  }
}
