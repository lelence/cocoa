package com.maogogo.cocoa.rest.socketio

case class SocketInMessage[T](event: String, t: T, headers: Map[String, String])

trait SocketOutMessage

case class BroadcastMessage[T](event: String, t: T) extends SocketOutMessage

case class EventMessage[T](event: String, t: T) extends SocketOutMessage

case class ReplayMessage[T](t: T) extends SocketOutMessage

case object NoneMessage extends SocketOutMessage
