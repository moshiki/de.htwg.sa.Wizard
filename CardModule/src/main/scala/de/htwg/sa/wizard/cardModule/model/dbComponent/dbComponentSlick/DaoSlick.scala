package de.htwg.sa.wizard.cardModule.model.dbComponent.dbComponentSlick

import java.sql.Date

import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.CardStack
import de.htwg.sa.wizard.cardModule.model.cardComponent.{CardInterface, CardStackInterface}
import de.htwg.sa.wizard.cardModule.model.dbComponent.DaoInterface
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class DaoSlick() extends DaoInterface {
  // TODO: Docker-ENV for Docker-Database
  val database = Database.forURL(
    url = "jdbc:mysql://localhost:3306/cardmodule?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "wizard",
    password = "wizard"
  )

  val cardStackTable = TableQuery[CardStackTable]
  val cardTable = TableQuery[CardTable]

  val setup = DBIO.seq((
    cardStackTable.schema
    ++ cardTable.schema
  ).createIfNotExists)
  database.run(setup)

  override def load(cardStackInterface: CardStackInterface): CardStackInterface = {
    val cardStackIdQuery = cardStackTable.sortBy(_.id.desc).take(1).result
    val cardStackId = Await.result(database.run(cardStackIdQuery), Duration.Inf)

    val cardQuery = cardTable.filter(_.cardStackId == cardStackId).sortBy(_.id.desc).result
    val cardResult = Await.result(database.run(cardQuery), Duration.Inf)
    var cardList: List[CardInterface] = Nil
    cardResult.foreach(card => {cardList = CardInterface.buildCard(card._2, card._3, card._4, card._5) :: cardList})
    CardStack(cardList)
  }

  override def save(cardStackInterface: CardStackInterface): Unit = {
    Await.ready(database.run(cardStackTable += (0, Date)), Duration.Inf)

    val cardStackIdQuery = cardStackTable.sortBy(_.id.desc).take(1).map(_.id).result.head
    val cardStackId = Await.result(database.run(cardStackIdQuery), Duration.Inf)

    cardStackInterface.cards.foreach(card => {
      Await.ready(database.run(cardTable += (0, card.typeString, card.colorOption, card.owner, card.value, cardStackId)), Duration.Inf)
    })
  }
}
