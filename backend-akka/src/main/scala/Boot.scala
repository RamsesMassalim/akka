import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

object Boot extends App {

  implicit val log: Logger = LoggerFactory.getLogger(getClass)

  val rootBehavior = Behaviors.setup[Nothing] { context =>

    implicit val ec = context.executionContext
    implicit val sys = context.system

    val mockTodos:Seq[Todo] = Seq(
      Todo("1","title1","description1",true),
      Todo("2","title2","description2",false)
    )

    val todos = new InMemoryTodoRepository(mockTodos)
    val router = new MyRouter(todos)

    MyServer.startHttpServer(router.route)
    Behaviors.empty
  }
  val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
}