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

package com.maogogo.cocoa.rest.endpoints

import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse, StatusCodes }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ ExceptionHandler, Route }
import akka.pattern.AskTimeoutException
import akka.util.Timeout
import com.google.inject.Inject
import com.maogogo.cocoa.rest.http.Json4sSupport
import com.typesafe.scalalogging.LazyLogging
import akka.pattern.ask
import com.maogogo.cocoa.common.cluster.ProxyActor

import scala.concurrent.duration._

class RootEndpoint @Inject() (proxy: ProxyActor) extends Json4sSupport with LazyLogging {

  implicit val timeout = Timeout(3 seconds)

  private val exceptionHandler = ExceptionHandler {
    case e: AskTimeoutException ⇒
      logger.error("", e)
      complete(errorResponse("Timeout or Has no routee"))
    case e: Throwable ⇒
      logger.error("", e)
      complete(errorResponse(e.getMessage))
  }

  //  private val myUserPassAuthenticator = (credentials: Credentials) ⇒ {
  //    credentials match {
  //      case p @ Credentials.Provided(id) if p.verify("p4ssw0rd") => Some(id)
  //      case _ => None
  //    }
  //  }

  def apply(): Route = {
    // (authenticateBasic("Basic", myUserPassAuthenticator)
    handleExceptions(exceptionHandler) {
      route
    }
  }

  private def route: Route = {
    pathEndOrSingleSlash {
      get {
        val dd = proxy.ref("HelloActor").get
        //
        val f = (dd ? "Toan").mapTo[String]

        complete(f)
      }
    }
  }

  private def errorResponse(msg: String): HttpResponse = {
    HttpResponse(
      StatusCodes.InternalServerError,
      entity = HttpEntity(
        ContentTypes.`application/json`,
        s"""{"jsonrpc":"2.0", "error": {"code": 500, "message": "${
          msg
            .replaceAll("\"", "\\\"")
        }"}}"""))
  }

}