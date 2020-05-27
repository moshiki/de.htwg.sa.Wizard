import play.api.libs.json.Json

val trumpColor: Option[String] = None

val str = Json.toJson(trumpColor)

Json.fromJson(str).asOpt[String]