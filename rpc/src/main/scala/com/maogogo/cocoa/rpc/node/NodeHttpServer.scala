/*
 * Copyright 2018 Maogogo Workshop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maogogo.cocoa.rpc.node

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import akka.http.scaladsl.server.Directives._
import com.google.inject.Inject
import com.google.inject.name.Named

class NodeHttpServer @Inject() (@Named("cluster_actor_map") actorMap: Map[String, ActorRef])(
  implicit
  system: ActorSystem,
  mat: ActorMaterializer) extends Json4sSupport with LazyLogging {

  import system.dispatcher

  lazy val route: Route =
    pathEndOrSingleSlash {
      get {
        val result = actorMap.map {
          case (k, ref) ⇒ (k → ref.path.toString)
        }
        complete(result)
      }
    } ~ path(Segment) { id ⇒

      complete("id ==>>" + id)

    }

  val bind = Http().bindAndHandle(route, interface = "0.0.0.0", port = 8989)

  bind.onComplete {
    case scala.util.Success(binding) ⇒ logger.info(s"node manager http server started! ${binding.localAddress}")
    case scala.util.Failure(ex) ⇒ logger.error("node manager http server start failed!", ex)
  }

}
