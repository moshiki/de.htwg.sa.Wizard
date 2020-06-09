package de.htwg.se.wizard.model.dbComponent.dbComponentSlick

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardInterface
import de.htwg.se.wizard.model.dbComponent.DaoInterface
import de.htwg.se.wizard.model.modelComponent.{ModelInterface, Player}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class DaoSlick() extends DaoInterface {
  // TODO: Docker-ENV for Docker-Database
  val database = Database.forURL(
    url = "jdbc:mysql://localhost:3306/wizard?serverTimezone=UTC",
    driver = "com.mysql.cj.jdbc.Driver",
    user = "wizard",
    password = "wizard"
  )

  val cardTable = TableQuery[CardTable]
  val playerTable = TableQuery[PlayerTable]
  val predictionPerRoundTable = TableQuery[PredictionPerRoundTable]
  val roundManagerTable = TableQuery[RoundManagerTable]
  val tricksPerRoundTable = TableQuery[TricksPerRoundTable]
  val controllerStateTable = TableQuery[ControllerStateTable]

  val setup = DBIO.seq((
    cardTable.schema
    ++ playerTable.schema
    ++ predictionPerRoundTable.schema
    ++ roundManagerTable.schema
    ++ tricksPerRoundTable.schema
    ++ controllerStateTable.schema
  ).createIfNotExists)
  database.run(setup)

  override def load(modelInterface: ModelInterface): (ModelInterface, String) = {
    val controllerStateQuery = controllerStateTable.sortBy(_.id.desc).take(1).map(_.state).result.head
    val controllerStateString = Await.result(database.run(controllerStateQuery), Duration.Inf)

    val roundManagerQuery = roundManagerTable.sortBy(_.id.desc).take(1).result.head
    val roundManagerTuple = Await.result(database.run(roundManagerQuery), Duration.Inf)
    val roundManagerId = roundManagerTuple._1

    val playerQuery = playerTable.filter(_.roundManagerId === roundManagerId).result
    val players = Await.result(database.run(playerQuery), Duration.Inf).map(playerTuple => {
      val id = playerTuple._1
      val cardQuery = cardTable.filter(card => card.playerId === id).result
      val cardTuples = Await.result(database.run(cardQuery), Duration.Inf)
      val cards = cardTuples.map(tuple => CardInterface.buildCard(tuple._2, tuple._3, tuple._4, tuple._5)).toList
      Player(playerTuple._2, cards)
    }).toList

    val predictionPerRoundQuery = predictionPerRoundTable.filter(_.roundManagerId === roundManagerId).map(_.prediction).result
    val predictionsPerRound = Await.result(database.run(predictionPerRoundQuery), Duration.Inf).toList

    val tricksPerRoundQuery = tricksPerRoundTable.filter(_.roundManagerId === roundManagerId).result
    val tricksPerRound = Await.result(database.run(tricksPerRoundQuery), Duration.Inf).map(tuple => tuple._2 -> tuple._3).toMap

    val playedCardsQuery = cardTable.filter(_.roundManagerId === roundManagerId).result
    val playedCards = Await.result(database.run(playedCardsQuery), Duration.Inf).map(tuple => CardInterface.buildCard(tuple._2, tuple._3, tuple._4, tuple._5)).toList

    val newModelInterface = modelInterface.buildModel(roundManagerTuple._2, roundManagerTuple._3, players, roundManagerTuple._4, roundManagerTuple._5, predictionsPerRound, tricksPerRound, playedCards, roundManagerTuple._6)
    (newModelInterface, controllerStateString)
  }

  override def save(modelInterface: ModelInterface, controllerState: String): Unit = {
    Await.ready(database.run(controllerStateTable += (0, controllerState)), Duration.Inf)
    Await.ready(database.run(roundManagerTable += (0, modelInterface.numberOfPlayers, modelInterface.numberOfRounds, modelInterface.currentPlayerNumber, modelInterface.currentRound, modelInterface.predictionMode)), Duration.Inf)

    val roundManagerIdQuery = roundManagerTable.sortBy(_.id.desc).take(1).map(_.id).result.head
    val roundManagerId = Await.result(database.run(roundManagerIdQuery), Duration.Inf)

    modelInterface.players.foreach(player => {
      Await.ready(database.run(playerTable += (0, player.name, roundManagerId)), Duration.Inf)
      val playerIdQuery = playerTable.sortBy(_.id.desc).take(1).map(_.id).result.head
      val playerId = Await.result(database.run(playerIdQuery), Duration.Inf)

      Await.ready(database.run(cardTable ++= player.playerCards.map(card => (0, card.typeString, card.colorOption, card.owner, card.value, Some(playerId), None))), Duration.Inf)
    })

    val insertPredictionsQuery = predictionPerRoundTable ++= modelInterface.predictionPerRound.map(prediction => (0, prediction, roundManagerId))
    Await.ready(database.run(insertPredictionsQuery), Duration.Inf)

    val insertTricksQuery = tricksPerRoundTable ++= modelInterface.tricksPerRound.map(mapData => (0, mapData._1, mapData._2, roundManagerId))
    Await.ready(database.run(insertTricksQuery), Duration.Inf)

    val insertPlayedCardsQuery = cardTable ++= modelInterface.playedCards.map(card => (0, card.typeString, card.colorOption, card.owner, card.value, None, Some(roundManagerId)))
    Await.ready(database.run(insertPlayedCardsQuery), Duration.Inf)
  }
}
