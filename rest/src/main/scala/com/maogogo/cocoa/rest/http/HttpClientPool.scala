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
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.http.scaladsl.settings.{ ClientConnectionSettings, ConnectionPoolSettings }
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ ActorMaterializer, OverflowStrategy, QueueOfferResult }
import akka.stream.scaladsl.{ Flow, Keep, Sink, Source }

import scala.concurrent.{ Future, Promise }
import scala.util.{ Failure, Success, Try }

trait HttpClientPool {

  implicit val system: ActorSystem
  implicit val mat: ActorMaterializer
  implicit val ex = system.dispatcher

  // Http().cachedHostConnectionPool[Promise[HttpResponse]]("akka.io")
  private[http] val connection: Flow[(HttpRequest, Promise[HttpResponse]), (Try[HttpResponse], Promise[HttpResponse]), Http.HostConnectionPool]

  private[http] lazy val queue =
    Source.queue[(HttpRequest, Promise[HttpResponse])](100, OverflowStrategy.dropNew)
      .via(connection)
      .toMat(Sink.foreach({
        case ((Success(resp), p)) => p.success(resp)
        case ((Failure(e), p)) => p.failure(e)
      }))(Keep.left)
      .run()

  def dispatcher = (request: HttpRequest) ⇒ {
    val responsePromise = Promise[HttpResponse]()
    queue.offer(request -> responsePromise).flatMap {
      case QueueOfferResult.Enqueued => responsePromise.future
      case QueueOfferResult.Dropped => Future.failed(new RuntimeException("Queue overflowed. Try again later."))
      case QueueOfferResult.Failure(ex) => Future.failed(ex)
      case QueueOfferResult.QueueClosed => Future.failed(new RuntimeException("Queue was closed (pool shut down) while running the request. Try again later."))
    }
  }

  def https(
    host: String,
    port: Int = 443,
    proxy: Option[Boolean] = None)(
    implicit
    system: ActorSystem) = {
    Http().cachedHostConnectionPoolHttps(host, port, settings = proxySettings(proxy))
  }

  def http(
    host: String,
    port: Int = 80,
    proxy: Option[Boolean] = None)(
    implicit
    system: ActorSystem) = {
    Http().cachedHostConnectionPool(host, port, settings = proxySettings(proxy))
  }

  private[http] def proxySettings(proxy: Option[Boolean] = None) = {
    proxy match {
      case Some(true) ⇒
        val httpsProxy = ClientTransport.httpsProxy(
          InetSocketAddress.createUnresolved("127.0.0.1", 1087))
        ConnectionPoolSettings(system).withTransport(httpsProxy)
      case _ ⇒ ConnectionPoolSettings(system)
    }
  }

  def get[T](uri: String)(implicit fallback: HttpResponse ⇒ Future[T]): Future[T] = {
    val fallbackFuture = (r: Future[HttpResponse]) ⇒ r.flatMap(fallback)
    (dispatcher andThen fallbackFuture)(Get(uri))
  }

  implicit class ResponseTo(response: HttpResponse) extends Unmarshal[HttpResponse](response) {}

}
