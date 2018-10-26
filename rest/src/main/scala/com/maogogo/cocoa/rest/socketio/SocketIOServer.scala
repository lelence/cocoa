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

package com.maogogo.cocoa.rest.socketio

import akka.Done
import akka.actor.{ ActorSystem, Props }
import akka.util.Timeout
import com.corundumstudio.socketio.listener.DataListener
import com.corundumstudio.socketio.{ AckRequest, Configuration }
import com.typesafe.scalalogging.LazyLogging
import org.json4s.jackson.JsonMethods

import scala.concurrent.Future
import scala.concurrent.duration._

object SocketIOServer {

}

class SocketIOServer(eventMessages: Seq[IOMessage[_]], port: Int, pool: Int = 10)(
  implicit
  system: ActorSystem) extends LazyLogging {

  implicit val timeout = Timeout(3 seconds)

  private lazy val router = system.actorOf(Props[SocketIORouter], "socketio_router")

  implicit val ex = system.dispatcher

  def bind(): Future[Done] = {

    val server = ioServer()

    server.addEventListener("", classOf[java.util.Map[String, Any]], new DataListener[java.util.Map[String, Any]] {
      override def onData(client: IOClient, data: java.util.Map[String, Any], ackSender: AckRequest): Unit = {

        val event = data.get("method").toString

        val json = JsonMethods.mapper.writeValueAsString(data.get("params"))

        logger.info(s"${client.getRemoteAddress} request: ${data}")

        eventMessages foreach {
          case e: EventMessage[_] ⇒ router ! e.copyTo(ackSender, json)
          case t ⇒ SocketIOClient.add(client, event, t.parameter(json))
        }
      }
    })

    // 这里要启动定时任务
    eventMessages.filter {
      case _: BroadcastMessage[_] ⇒ true
      case _ ⇒ false
    } foreach (router ! _)

    server.start()
    Future.successful(Done)
  }

  def broadMessage(event: String, anyRef: AnyRef): Unit = {
  }

  def broadMessage(event: String, anyRef: AnyRef, filter: String ⇒ Boolean): Unit = {

    val exClients = SocketIOClient.getClients(!filter(_)).map(_.client)

    ioServer().getBroadcastOperations.sendEvent(event, exClients, anyRef)
  }

  private lazy val ioConfig =
    (port: Int) ⇒ {
      val _config = new Configuration
      _config.setHostname("0.0.0.0")
      _config.setPort(port)
      _config.setMaxFramePayloadLength(1024 * 1024)
      _config.setMaxHttpContentLength(1024 * 1024)
      _config.getSocketConfig.setReuseAddress(true)
      _config
    }

  private lazy val ioServer =
    () ⇒ {
      val _server = new IOServer(ioConfig(port))
      _server.addConnectListener(new ConnectionListener)
      _server.addDisconnectListener(new DisconnectionListener)
      _server
    }

}
