package gatling

import gatling.CommandsSupport._
import gatling.ConfSupport._
import io.gatling.app.Gatling
import io.gatling.core.Predef._
import io.gatling.core.config.GatlingPropertiesBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.sys.process._

object GatlingTestRunner {

  def main(args: Array[String]): Unit = {

    val simClass = classOf[GatlingTest].getName

    val props = new GatlingPropertiesBuilder
    props.simulationClass(simClass)
    props.runDescription("GatlingTest Runner")
    props.resourcesDirectory("./src/main/scala")
    props.binariesDirectory("./target/scala-2.13/test-classes/gatling")
    //props.simulationsDirectory("./src/test/scala/gatling")

    Gatling.fromMap(props.build)
  }
}

class GatlingTest extends Simulation {

  private val title = "gatling-test"

  private val time     = 60
  private val users    = 60
  private val requests = 10
  private val injects  = 2

  private val unknownRequest = "GET unknown"
  private val nothingRequest = "GET nothing"
  private val sleepRequest   = "GET sleep"
  private val random1Request = "GET random1"
  private val random2Request = "GET random2"
  private val random3Request = "GET random3"

  private val unknown =
    http(unknownRequest)
      .get("/api/unknown")
      .check(status.is(200))

  private val nothing =
    http(nothingRequest)
      .get("/v1/nothing")
      .check(status.is(200))

  private val sleep =
    http(sleepRequest)
      .get("/v1/sleep")
      .check(status.is(200))

  private val random1 =
    http(random1Request)
      .get("/v1/random1")
      .check(status.is(200))

  private val random2 =
    http(random2Request)
      .get("/v1/random2")
      .check(status.is(200))

  private val random3 =
    http(random3Request)
      .get("/v1/random3")
      .check(status.is(200))

  private val unknownScenario = scenario(title)
    .tryMax(3) {
      exec(login)
    }
    .exec(repeat(requests) {
      exec(unknown)
    })

  private val nothingScenario = scenario(title)
    .exec(repeat(requests) {
      exec(nothing)
    })

  private val sleepScenario = scenario(title)
    .exec(repeat(requests) {
      exec(sleep)
    })

  private val random1Scenario = scenario(title)
    .exec(repeat(requests) {
      exec(random1)
    })

  private val random2Scenario = scenario(title)
    .exec(repeat(requests) {
      exec(random2)
    })

  private val random3Scenario = scenario(title)
    .exec(repeat(requests) {
      exec(random3)
    })

  before {
    if (enableProfiler.equals("enable")) {
      val msg = s"Starting profile ($title)"

      proc match {
        case p if p == error => println(p)
        case _ =>
          val list = proc.trim.replaceAll(" +", " ").split(" ")
          val pid  = list(1)

          println("PID: " + pid)

          val profiler =
            s"/root/async-profiler/profiler.sh -d $time -f /tmp/$title.svg --title $title -e itimer $pid"
          profiler.run()
      }
    }
  }

  setUp(
    unknownScenario
    // nothingScenario
    // sleepScenario
    // random1Scenario
    // random2Scenario
    // random3Scenario
      .inject(
        rampConcurrentUsers(0) to users during (time / injects seconds),
        constantConcurrentUsers(users) during (time / injects seconds)
      )
  ).maxDuration(time)
    .protocols(protocolReqres) // protocolLocalhost
    .assertions(
      details(unknownRequest).successfulRequests.percent.gte(99),
      details(unknownRequest).requestsPerSec.gte(requests)
    )
}

// unknownRequest
// nothingRequest
// sleepRequest
// random1Request
// random2Request
// random3Request
