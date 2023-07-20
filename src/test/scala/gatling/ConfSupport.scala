package gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

object ConfSupport {

  val enableProfiler: String = sys.env.getOrElse("PROFILER", "disable")

  val protocolReqres: HttpProtocolBuilder = http
    .baseUrl("https://reqres.in:443")
    .disableWarmUp
    .disableCaching

  val protocolLocalhost: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080")
    .disableWarmUp
    .disableCaching

  val login: HttpRequestBuilder =
    http("Login")
      .post("/api/login")
      .header("Content-Type", "application/json")
      .body(StringBody("""
       |{
       |    "email": "eve.holt@reqres.in",
       |    "password": "cityslicka"
       |}
     """.stripMargin))
      .check(status.is(200))

}
