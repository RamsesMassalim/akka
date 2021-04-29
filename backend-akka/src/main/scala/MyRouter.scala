import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{Directives, Route}

import scala.concurrent.ExecutionContext

trait  Router {
  def route:Route
}

class MyRouter(todoRepository: TodoRepository)(implicit system: ActorSystem[_],  ex:ExecutionContext)
  extends Router
    with  Directives
    with TodoDirectives
    with ValidatorDirectives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  override def route = concat(
    path("ping") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "pong"))
      }
    },
    path("todos") {
      pathEndOrSingleSlash {
        concat(
          get {
            handleWithGeneric(todoRepository.all()) {
              todos => complete(todos)
            }
          },
          post {
            entity(as[CreateTodo]) { createTodo =>
              validateWith(CreateTodoValidator)(createTodo){
                handleWithGeneric(todoRepository.create(createTodo)){todo => complete(todo)}
              }
            }
          }
        )
      }
    }
  )
}
//todo add update route
