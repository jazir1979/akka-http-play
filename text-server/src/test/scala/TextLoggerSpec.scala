import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import MediaTypes._

class TextLoggerSpec extends Specification with Specs2RouteTest with TextLoggerService {
  def actorRefFactory = system

  "MyService" should {

    "POST back whatever you /submit" in {
      Post("/submit", HttpEntity(`application/json`, "{ \"message\" : \"notification text\" }")) ~> myRoute ~> check {
        responseAs[String] must contain("notification text")
      }
    }

    "return a greeting for GET requests to the root path" in {
      Get() ~> myRoute ~> check {
        responseAs[String] must contain("Hello")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(myRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}