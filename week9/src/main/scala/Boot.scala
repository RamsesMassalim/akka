import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.{Logger, LoggerFactory}

object Boot extends App {
  implicit val log: Logger = LoggerFactory.getLogger(getClass)

  val rootBehavior: Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
    implicit val ec = context.executionContext
    implicit val sys = context.system

    val router = new MyRouter()

    MyServer.startHttpServer(router.route)
    Behaviors.empty
  }

  val system = ActorSystem[Nothing](rootBehavior, "calculator")
}