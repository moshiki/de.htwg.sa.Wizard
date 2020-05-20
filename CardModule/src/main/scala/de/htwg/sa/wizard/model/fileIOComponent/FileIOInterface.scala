package de.htwg.sa.wizard.model.fileIOComponent

import de.htwg.sa.wizard.model.cardComponent.CardInterface

import scala.util.Try

trait FileIOInterface {
  def load(cardInterface: CardInterface, path: String): Try[CardInterface]

  def save(cardInterface: CardInterface, path: String): Try[Unit]
}
