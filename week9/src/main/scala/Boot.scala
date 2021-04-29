import akka.actor.{ActorSystem, Props}
import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.{ActorSystem => AS}
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.{Logger, LoggerFactory}

object Boot extends App {
  implicit val log: Logger = LoggerFactory.getLogger(getClass)

  val rootBehavior: Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
    implicit val ec = context.executionContext
    implicit val sys = context.system

    val actor_system = AS("calc")
    val Calculator = actor_system.actorOf(Props(new Calculator), "calculator")
    val router = new Router(Calculator)

    MyServer.startHttpServer(router.route)
    Behaviors.empty
  }

  val system = akka.actor.typed.ActorSystem[Nothing](rootBehavior, "calculator")
}