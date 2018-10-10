package com.maogogo.cocoa.rest.endpoints

import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse, StatusCodes }
import akka.http.scaladsl.server.{ ExceptionHandler, Route }
import akka.http.scaladsl.server.Directives._
import akka.pattern.AskTimeoutException
import com.maogogo.cocoa.rest.Json4sSupport
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