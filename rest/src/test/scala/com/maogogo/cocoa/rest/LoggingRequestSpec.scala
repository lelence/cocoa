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

package com.maogogo.cocoa.rest

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.directives.{ DebuggingDirectives, LoggingMagnet }

class LoggingRequestSpec extends RestSpec {

  def printRequestMethod(req: HttpRequest): Unit = println(req.method.name)

  val logRequestPrintln = DebuggingDirectives.logRequest(LoggingMagnet(_ => printRequestMethod))

  "test1" in {
    Get("/") ~> logRequestPrintln(complete("logged")) ~> check {
      responseAs[String] shouldEqual "logged"
    }
  }
}
