import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.collection._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

case object Start
case class SetRequest(expr: String)
case class GetRequest(res: String)

object MainCalc extends Actor with ActorLogging {
        implicit val timeout = Timeout(5 seconds)
        val calculatorActor = context.actorOf(Props(Calculator), Calculator.name)

        override def preStart() {
            self ! Start
        }

        def receive = {
            case Start =>
                calculatorActor ! "dummy request"
                calculatorActor ! SetRequest("14+2+5+8*2/29=")
                val respF = calculatorActor ? GetRequest("Result")
                respF pipeTo self

            case r: GetRequest =>
                log.warning(s"Response: $r")
        }

}
