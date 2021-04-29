import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

import scala.io.StdIn.readLine

case object Start
case class SetRequest(expr: String)
case class GetRequest(res: String)
case class GetResponse(res: Double)

class MainCalc extends Actor with ActorLogging {
        implicit val timeout = Timeout(5 seconds)

        val calculatorActor = context.actorOf(Props(new Calculator), "Calc")

        override def preStart() {
            self ! Start
        }

        def receive = {
            case Start =>
                val message = readLine()
                log.info(s"Request: $message")
                calculatorActor ! SetRequest(message)
                val respF = calculatorActor ? GetRequest("Result")
                respF pipeTo self

            case r: GetResponse =>
                // log.warning(s"Response: ${r.res}")
                log.info(s"Response: ${r.res}")
        }
}
