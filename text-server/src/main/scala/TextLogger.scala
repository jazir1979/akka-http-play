import akka.actor.Actor
import spray.http._
import spray.routing._
import MediaTypes._

class TextLogger extends Actor with TextLoggerService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

trait TextLoggerService extends HttpService {

  // TODO: json
  val myRoute: Route =
    path("") {
      post {
        entity(as[String]) { str =>
          complete(s"You sent us: ${str}")
        }
      }
    }
}
