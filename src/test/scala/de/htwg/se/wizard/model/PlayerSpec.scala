package de.htwg.se.wizard.model

import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers {

    "A Player" when { "new" should {
      val player = Player("Name ")
      "have a name"  in {
        player.name should be("Name")
      }
      "have a nice String representation" in {
        player.toString should be("Name")
      }
    }}

}
