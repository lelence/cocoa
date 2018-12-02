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

package com.maogogo.cocoa.rest.http

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
