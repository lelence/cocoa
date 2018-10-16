package com.maogogo.cocoa.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.maogogo.cocoa.rest.endpoints.RootEndpoint
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject

class HttpServer @Inject() (root: RootEndpoint)(
  implicit
  system: ActorSystem,
  mat: ActorMaterializer) extends LazyLogging {

  import system.dispatcher

  val bind = Http().bindAndHandle(root(), interface = "0.0.0.0", port = 9000)

  bind.onComplete {
    case scala.util.Success(binding) ⇒ logger.info(s"http server started! ${binding.localAddress}")
    case scala.util.Failure(ex) ⇒ logger.error("http server start failed!", ex)
  }

}
