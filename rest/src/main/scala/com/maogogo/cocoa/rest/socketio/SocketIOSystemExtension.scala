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

import akka.actor.{ ActorRef, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider, Props }
import akka.routing.RoundRobinPool
import com.corundumstudio.socketio.{ AckRequest, Configuration, SocketIOClient }
import com.google.inject.Injector
import com.typesafe.config.Config

object SocketIOSystemExtension extends ExtensionId[SocketIOSystemExtensionImpl] with ExtensionIdProvider {

  override def createExtension(system: ExtendedActorSystem): SocketIOSystemExtensionImpl = {

    val router = system.actorOf(RoundRobinPool(10).props(Props[SocketIOServerActor]), "socketio_router")
    new SocketIOSystemExtensionImpl(router)
  }

  override def lookup() = SocketIOSystemExtension

  override def get(system: ActorSystem): SocketIOSystemExtensionImpl = super.get(system)
}

class SocketIOSystemExtensionImpl(router: ActorRef) extends Extension {

  // private lazy val injectorLocal: ThreadLocal[Injector] = ThreadLocal.withInitial(() ⇒ null)

  // private lazy val serverLocal: ThreadLocal[com.corundumstudio.socketio.SocketIOServer] = ThreadLocal.withInitial(() ⇒ null)

  def init(injector: Injector): SocketIOServer = {
    // injectorLocal.set(injector)

    val server = new com.corundumstudio.socketio.SocketIOServer(config)
    server.addConnectListener(new ConnectionListener)
    server.addDisconnectListener(new DisconnectionListener)

    // serverLocal.set(server)

    new SocketIOServer(injector, server, router)
  }

  import net.codingwell.scalaguice.InjectorExtensions._

  private lazy val config: Configuration = {
    val _config = new Configuration
    _config.setHostname("0.0.0.0")
    _config.setPort(9092)
    _config.setMaxFramePayloadLength(1024 * 1024)
    _config.setMaxHttpContentLength(1024 * 1024)
    _config.getSocketConfig.setReuseAddress(true)
    _config
  }

  //  private lazy val server = {
  //    val _server = new com.corundumstudio.socketio.SocketIOServer(config)
  //    _server.addConnectListener(new ConnectionListener)
  //    _server.addDisconnectListener(new DisconnectionListener)
  //    _server
  //  }

}
