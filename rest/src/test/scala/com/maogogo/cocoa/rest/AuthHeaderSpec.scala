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

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials

import scala.concurrent.Future

class AuthHeaderSpec extends RestSpec {

  def myUserPassAuthenticatorAsync(credentials: Credentials): Future[Option[String]] =
    credentials match {
      case p @ Credentials.Provided(id) =>
        Future {
          // potentially
          if (p.verify("p4ssw0rd")) Some(id)
          else None
        }
      case _ => Future.successful(None)
    }

  def myUserPassAuthenticator(credentials: Credentials): Option[String] =
    credentials match {
      case p @ Credentials.Provided(id) if p.verify("p4ssw0rd", (s: String) ⇒ {
        println("s ===>>>>" + s)
        s
      }) =>
        println("===>>>>" + id)
        Some(id)
      case _ => None
    }

  val route =
    Route.seal {
      path("secured") {
        authenticateBasic(realm = "secure site", myUserPassAuthenticator) { userName =>
          complete(s"The user is '$userName'")
        }
      } ~ path("secured2") {
        authenticateBasicAsync(realm = "secure site", myUserPassAuthenticatorAsync) { userName =>
          complete(s"The user is '$userName'")
        }
      }
    }

  "test1" in {

    Get("/secured") ~> route ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The resource requires authentication, which was not supplied with the request"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("secure site"), Map("charset" → "UTF-8"))
    }

  }

  "test2" in {
    val validCredentials = BasicHttpCredentials("John", "p4ssw0rd")

    Get("/secured") ~> addCredentials(validCredentials) ~> // adds Authorization header
      route ~> check {
      responseAs[String] shouldEqual "The user is 'John'"
    }

  }

  "test3" in {
    val invalidCredentials = BasicHttpCredentials("Peter", "pan")
    Get("/secured") ~>
      addCredentials(invalidCredentials) ~> // adds Authorization header
      route ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The supplied authentication is invalid"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("secure site"), Map("charset" → "UTF-8"))
    }
  }

}
