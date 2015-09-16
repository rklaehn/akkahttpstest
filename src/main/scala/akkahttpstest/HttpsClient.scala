package akkahttpstest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{Uri, HttpRequest}
import akka.stream.ActorMaterializer

import scala.util.{Success, Failure}

object HttpsClient extends App {

  implicit val system = ActorSystem("secureclient")
  implicit val materializer = ActorMaterializer()
  import system._

  Http().singleRequest(HttpRequest(uri = Uri("https://127.0.0.1:8081")), httpsContext = Some(DebugHttpsContexts.trustfulClientContext)).onComplete {
    case Success(r) ⇒ println(r)
    case Failure(e) ⇒ e.printStackTrace()
  }
}