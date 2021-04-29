import akka.actor.{Actor, Props}
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.{Directives, Route}
import akka.pattern.ask
import akka.util.Timeout
import akka.http.scaladsl.server.Directives._

import scala.concurrent.duration._
import scala.language.postfixOps

import scala.concurrent.ExecutionContext

class MyRouter(implicit system: ActorSystem[_],  ex:ExecutionContext) extends Actor with Directives {
  implicit val timeout = Timeout(5 seconds)

  def route: Route = concat(
    path("/"){
      val MainCalcActor = context.actorOf(Props(new MainCalc), "MainCalculator")
      concat(
        path(String) { expr =>
          MainCalcActor ! SetRequest(expr.toString)
          val result = MainCalcActor ? GetRequest("Result")

        }
      )
    }
  )

  def getRoute : Route = get {
    path("dataSourceByName") {
      parameters('name.as[String]) {
        (name) =>
        ...
      }
    }
  }
}
