import de.htwg.sa.wizard.cardModule.controller.controllerComponent.controllerBaseImplementation.CardController
import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.CardStack
import de.htwg.sa.wizard.cardModule.model.fileIOComponent.FileIOJSON.FileIO
import play.api.libs.json._

val controller = CardController(CardStack(), FileIO())
controller.trumpColor