package akkahttpstest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

object HttpsServer extends App {
  implicit val system = ActorSystem("secureserver")
  implicit val materializer = ActorMaterializer()
  import system._

  val serverSource = Http().bind(interface = "0.0.0.0", port = 8081, httpsContext = Some(DebugHttpsContexts.serverContext))
  val bindingFuture = serverSource.to(Sink.foreach { connection =>
    // foreach materializes the source
    println("Accepted new connection from " + connection.remoteAddress)
    // ... and then actually handle the connection
    connection.handleWithSyncHandler(requestHandler)
  }).run()

  def requestHandler(request:HttpRequest) : HttpResponse = {
    println(request)
    HttpResponse(entity = request.toString)
  }
}
