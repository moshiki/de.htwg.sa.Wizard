package de.htwg.se.wizard

import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.cards.WizardCard
import de.htwg.se.wizard.model.modelComponent.modelBaseImpl.{ResultTable, RoundManager}
import play.api.libs.json._

object jsontest {
  def main(args: Array[String]): Unit = {
    /*val resultTable = ResultTable.initializeTable()
    val json = Json.toJson(resultTable)
    val table2 = json.validate[ResultTable].asOpt.get

    println(resultTable == table2)*/

    val roundManager = RoundManager(resultTable = ResultTable.initializeTable())
    val json = Json.toJson(roundManager)
    println(json)
    /*val rm2 = json.validate[RoundManager].asOpt.get

    println(json)
    println()
    println(roundManager == rm2)*/
  }
}
