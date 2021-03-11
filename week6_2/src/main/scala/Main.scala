import akka.actor._

object Main extends App {
  val system = ActorSystem("system")
  val mainActor = system.actorOf(Props(MainCalc), "mainActor")
}
