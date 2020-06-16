import de.htwg.sa.wizard.cardModule.model.cardComponent.CardStackInterface
import de.htwg.sa.wizard.cardModule.model.cardComponent.cardBaseImplementation.CardStack
import play.api.libs.json.Json

val cs = CardStack()

println(cs.cards)

val json = Json.toJson(cs.cards)

println(Json.prettyPrint(json))

