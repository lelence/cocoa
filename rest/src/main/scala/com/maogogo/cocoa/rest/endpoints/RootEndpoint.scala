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
import akka.http.scaladsl.server.{ ExceptionHandler, Route }
import akka.http.scaladsl.server.Directives._
import akka.pattern.AskTimeoutException
import com.maogogo.cocoa.rest.http.Json4sSupport
import com.typesafe.scalalogging.LazyLogging

class RootEndpoint extends Json4sSupport with LazyLogging {

  private val exceptionHandler = ExceptionHandler {
    case e: AskTimeoutException ⇒
      logger.error("", e)
      complete(errorResponse("Timeout or Has no routee"))
    case e: Throwable ⇒
      logger.error("", e)
      complete(errorResponse(e.getMessage))
  }

  def apply(): Route = {
    handleExceptions(exceptionHandler)(route)
  }

  private def route: Route = {
    pathEndOrSingleSlash {
      get {
        complete("hello")
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