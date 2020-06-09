package de.htwg.sa.wizard.cardModule.model.dbComponent

import de.htwg.sa.wizard.cardModule.model.cardComponent.CardStackInterface

trait DaoInterface {
  def load(): CardStackInterface

  def save(cardStackInterface: CardStackInterface): Unit
}
