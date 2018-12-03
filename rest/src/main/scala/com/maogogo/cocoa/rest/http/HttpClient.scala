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

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.http.scaladsl.{ ClientTransport, Http }
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, HttpResponse, Uri }
import akka.http.scaladsl.settings.ClientConnectionSettings
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Flow, Sink, Source }

import scala.concurrent.{ Future, Promise }
import scala.util.Try

trait HttpClient {

  implicit val system: ActorSystem
  implicit val mat: ActorMaterializer
  implicit val ex = system.dispatcher

  private[http] val connection: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]]

  private[http] val dispatcher = (request: HttpRequest) ⇒
    Source.single(request).via(connection).runWith(Sink.head)

  def https(
    host:  String,
    port:  Int             = 443,
    proxy: Option[Boolean] = None
  )(
    implicit
    system: ActorSystem
  ): Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
    Http().outgoingConnectionHttps(host, port, settings = proxySettings(proxy))
  }

  def http(
    host:  String,
    port:  Int             = 80,
    proxy: Option[Boolean] = None
  )(
    implicit
    system: ActorSystem
  ): Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
    Http().outgoingConnection(host, port, settings = proxySettings(proxy))
  }

  private[http] def proxySettings(proxy: Option[Boolean] = None) = {
    proxy match {
      case Some(true) ⇒
        val httpsProxy = ClientTransport.httpsProxy(
          InetSocketAddress.createUnresolved("127.0.0.1", 1087)
        )
        ClientConnectionSettings(system).withTransport(httpsProxy)
      case _ ⇒ ClientConnectionSettings(system)
    }
  }

  def get[T](uri: String)(implicit fallback: HttpResponse ⇒ Future[T]): Future[T] = {
    val fallbackFuture = (r: Future[HttpResponse]) ⇒ r.flatMap(fallback)
    (dispatcher andThen fallbackFuture)(Get(uri))
  }

  implicit class ResponseTo(response: HttpResponse) extends Unmarshal[HttpResponse](response) {}

}
