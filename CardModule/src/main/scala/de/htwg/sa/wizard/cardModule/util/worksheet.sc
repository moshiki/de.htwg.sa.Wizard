import play.api.libs.json.Json

val trumpColor: Option[String] = None

Json.toJson(trumpColor).toString