package de.htwg.sa.wizard.resultTable.controllerComponent.controllerBaseImplementation

import com.google.inject.Inject
import de.htwg.sa.wizard.resultTable.controllerComponent.ResultTableControllerInterface
import de.htwg.sa.wizard.resultTable.model.fileIOComponent.FileIOInterface
import de.htwg.sa.wizard.resultTable.model.resultTableComponent.ResultTableInterface

import scala.util.{Failure, Success}

case class ResultTableController @Inject()(var resultTableInterface: ResultTableInterface, fileIOInterface: FileIOInterface) extends ResultTableControllerInterface {
  def updatePoints(round: Int, points: Vector[Int]): Unit = for (playerWhosePointsGetUpdated <- points.indices) {
    resultTableInterface = resultTableInterface.updatePoints(round, playerWhosePointsGetUpdated, points(playerWhosePointsGetUpdated))
  }

  def initializeTable(numberOfRounds: Int, numberOfPlayers: Int): Unit = {
    resultTableInterface = resultTableInterface.initializeTable(numberOfRounds, numberOfPlayers)
  }

  def safe(): Unit = {
    fileIOInterface.save(resultTableInterface, "ResultTableModule")
  }

  def load(): Unit = {
    resultTableInterface = fileIOInterface.load(resultTableInterface, "ResultTableModule") match {
      case Failure(_) => return
      case Success(resultTable) => resultTable
    }
  }

  def pointArrayForView: Array[Array[Any]] = resultTableInterface.toAnyArray

  def tableAsString: String = resultTableInterface.toString

  override def playerList: List[String] = resultTableInterface.playerNameList
}
