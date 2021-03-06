

class DatabaseTest extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:1233")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36")

	val headers_0 = Map(
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1")

	val headers_1 = Map(
		"Origin" -> "http://localhost:1233",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1")



	val scn = scenario("DatabaseTest")
		.exec(http("request_0")
			.get("/wizard")
			.headers(headers_0))
		.pause(2)
		.exec(http("request_1")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"))
		.pause(2)
		.exec(http("request_2")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "3"))
		.pause(1)
		.exec(http("request_3")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"))
		.pause(1)
		.exec(http("request_4")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "2")
			.resources(http("request_5")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "3")))
		.pause(1)
		.exec(http("request_6")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1")
			.resources(http("request_7")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"),
            http("request_8")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1")))
		.pause(1)
		.exec(http("request_9")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1")
			.resources(http("request_10")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "")))
		.pause(1)
		.exec(http("request_11")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"))
		.pause(1)
		.exec(http("request_12")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"))
		.pause(5)
		.exec(http("request_13")
			.get("/wizard/save")
			.headers(headers_0))
		.pause(4)
		.exec(http("request_14")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"))
		.pause(1)
		.exec(http("request_15")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1")
			.resources(http("request_16")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1")))
		.pause(1)
		.exec(http("request_17")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"))
		.pause(1)
		.exec(http("request_18")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1")
			.resources(http("request_19")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"),
            http("request_20")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"),
            http("request_21")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1"),
            http("request_22")
			.post("/wizard")
			.headers(headers_1)
			.formParam("input", "1")))
		.pause(4)
		.exec(http("request_23")
			.get("/wizard/load")
			.headers(headers_0))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}