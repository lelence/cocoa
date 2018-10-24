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

import akka.actor.ActorRef
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse, StatusCodes }
import akka.http.scaladsl.server.{ ExceptionHandler, Route }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.Credentials
import akka.pattern.AskTimeoutException
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.Inject
import com.google.inject.name.Named
import com.maogogo.cocoa.rest.http.Json4sSupport
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration._

class RootEndpoint @Inject() (@Named("uhaha") proxy: ActorRef) extends Json4sSupport with LazyLogging {

  implicit val timeout = Timeout(3 seconds)

  private val exceptionHandler = ExceptionHandler {
    case e: AskTimeoutException ⇒
      logger.error("", e)
      complete(errorResponse("Timeout or Has no routee"))
    case e: Throwable ⇒
      logger.error("", e)
      complete(errorResponse(e.getMessage))
  }

  private val myUserPassAuthenticator = (credentials: Credentials) ⇒ {
    credentials match {
      case p @ Credentials.Provided(id) if p.verify("p4ssw0rd") => Some(id)
      case _ => None
    }
  }

  def apply(): Route = {
    handleExceptions(exceptionHandler)(authenticateBasic("Basic", myUserPassAuthenticator) { userName ⇒
      println("user name ====>>>" + userName)
      route
    })
  }

  private def route: Route = {
    pathEndOrSingleSlash {
      get {
        complete((proxy ? "haha").mapTo[String])
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