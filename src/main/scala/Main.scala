import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object HelloWorld extends App {

  implicit val system = ActorSystem("ProxySystem")
  implicit val mat = ActorMaterializer()

  val route: Route = get {
    pathSingleSlash {
      complete(
        "Hello World\n" +
        "scala: " + scala.util.Properties.versionString + "\n" +
        "java: " + scala.util.Properties.javaVersion)
    }
  }
  val port = (scala.util.Properties.envOrElse("PORT", "8080")).toInt
  val bindingFuture = Http().bindAndHandle(Route.handlerFlow(route), "0.0.0.0", port = port)

  Await.result(system.whenTerminated, Duration.Inf)

}
