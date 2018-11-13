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

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.http.scaladsl.ClientTransport
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, Uri }
import akka.http.scaladsl.settings.ClientConnectionSettings

package object http {

  object Get {
    def apply(uri: String): HttpRequest =
      HttpRequest(method = HttpMethods.GET, uri = Uri(uri))

    def apply(uri: Uri): HttpRequest =
      HttpRequest(method = HttpMethods.GET, uri = uri)
  }

  object Post {

    def apply(uri: String): HttpRequest =
      HttpRequest(method = HttpMethods.POST, uri = Uri(uri))

    def apply(uri: Uri): HttpRequest =
      HttpRequest(method = HttpMethods.POST, uri = uri)

  }

}
