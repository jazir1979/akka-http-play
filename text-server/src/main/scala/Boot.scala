import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._
import spray.can.Http

object Boot extends App {
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[TextLogger], "text-logger")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
