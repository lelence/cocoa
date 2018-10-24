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

import akka.http.scaladsl.model.{ HttpHeader, HttpResponse }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{ Matchers, WordSpec }
import akka.http.scaladsl.server._
import akka.http.scaladsl.model._

abstract class RestSpec extends WordSpec with Matchers with ScalatestRouteTest with Directives {

  val myExceptionHandler = ExceptionHandler {
    case _: ArithmeticException =>
      extractUri { uri =>
        println(s"Request to $uri could not be handled normally")
        complete(HttpResponse(StatusCodes.InternalServerError, entity = "Bad numbers, bad result!!!"))
      }
  }

  // 注意这里需要小写
  val apiKey = "apiKey".toLowerCase

  def extractExampleHeader: HttpHeader => Option[String] = {
    case HttpHeader(`apiKey`, value) => Some(value)
    case _ => None
  }

  val root = handleExceptions(myExceptionHandler) {
    get {
      pathEndOrSingleSlash {
        (headerValueByName("X-User-Id") | provide("haha")) { value ⇒
          println("hhhhhhhhh=> " + value)
          complete(s"hello:$value")
        }
      }
    } ~ path("hh") {
      (headerValue(extractExampleHeader) | provide("dudu")) { value ⇒
        complete(s"hello:$value")
      }
    }
  }

}
