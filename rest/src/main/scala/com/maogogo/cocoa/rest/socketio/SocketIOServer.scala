package com.maogogo.cocoa.rest.socketio

import akka.Done
import akka.actor.ActorRef
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{ Sink, Source }
import com.corundumstudio.socketio.Configuration
import com.maogogo.cocoa.rest.socketio_bak.{ ConnectionListener, DisconnectionListener, IOServer }
import com.typesafe.config.Config

import scala.concurrent.Future

object SocketIOServer {

//  lazy val defPort = 9099
//
//  lazy val defPool = 10
//
//  def apply(config: Config): SocketIOServer = {
//
//    val port = if (config.hasPath("socketio.port")) config.getInt("socketio.port") else defPort
//
//    val pool = if (config.hasPath("socketio.port")) config.getInt("socketio.port") else defPool
//
//    apply(port, pool)
//  }
//
//  def apply(port: Int = defPort, pool: Int = defPool): SocketIOServer = new SocketIOServer(port, pool)

}

class SocketIOServer {

  def bind(): Future[Done] = {






    Future.successful(Done)
  }

//  private lazy val config = (port: Int) ⇒ {
//    val _config = new Configuration
//    _config.setHostname("0.0.0.0")
//    _config.setPort(port)
//    _config.setMaxFramePayloadLength(1024 * 1024)
//    _config.setMaxHttpContentLength(1024 * 1024)
//    _config.getSocketConfig.setReuseAddress(true)
//    _config
//  }
//
//  val server = new IOServer(config(port))
//  server.addConnectListener(new ConnectionListener)
//  server.addDisconnectListener(new DisconnectionListener)



}
