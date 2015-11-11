import akka.actor.Actor
import spray.http._
import spray.httpx.SprayJsonSupport
import spray.routing._
import MediaTypes._
import spray.json._
import DefaultJsonProtocol._

case class MobileNotificationDTO(message: String)

object MobileNotificationJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val mobileNotificationFormat = jsonFormat1(MobileNotificationDTO)
}

import MobileNotificationJsonProtocol._

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

  val myRoute: Route =
    path("submit") {
      post {
        entity(as[MobileNotificationDTO]) { dto =>
          complete(s"You sent us: ${dto.message}")
        }
      }
    } ~ path("") {
        get {
          respondWithMediaType(`text/html`) {
            complete {
              <html> <body> <h1>Hello!</h1> </body> </html>
            }
          }
        }
    }
}
