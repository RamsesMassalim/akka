import scala.util.{Failure, Success}
import akka.actor.{Actor, ActorRef, Props, TypedActor, TypedProps}
import akka.actor.TypedActor.context
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.{Directives, Route}
import akka.pattern.ask
import akka.util.Timeout

import scala.language.postfixOps
import scala.concurrent.ExecutionContext
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.duration.DurationInt

class Router(calculator: ActorRef)(implicit system: ActorSystem[_],  ex:ExecutionContext) extends Directives {
  implicit val timeout = Timeout(1 seconds)

  def route: Route = concat(
    path("calculate"){
      parameters('expr.as[String]) {
        (expr) =>
          var message = ""
          Calculator ! SetRequest(expr)
          val calculated = Calculator ? GetRequest("")
          calculated onComplete {
            case Success(value: GetResponse) =>
              message = value.res.toString
              system.log.info("Result of calculation: " + message)
            case Failure(error) =>
              message = "sadness"
              system.log.error("Calculation was failure, error: " + error)
          }
          Thread.sleep(1000)
          complete(message)
      }
    }
  )
}