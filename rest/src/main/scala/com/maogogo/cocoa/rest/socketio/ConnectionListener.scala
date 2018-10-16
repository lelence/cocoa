package com.maogogo.cocoa.rest.socketio

import com.corundumstudio.socketio.SocketIOClient
import org.slf4j.LoggerFactory

class ConnectionListener extends com.corundumstudio.socketio.listener.ConnectListener {

  lazy val logger = LoggerFactory.getLogger(getClass)

  override def onConnect(client: SocketIOClient): Unit = {
    logger.info(s"SocketIO: remote ${client.getRemoteAddress.toString} has connected")
  }
}

class DisconnectionListener extends com.corundumstudio.socketio.listener.DisconnectListener {

  lazy val logger = LoggerFactory.getLogger(getClass)

  override def onDisconnect(client: SocketIOClient): Unit = {
    logger.info(s"SocketIO: remote ${client.getRemoteAddress.toString} has disconnected")
  }
}
