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

import akka.actor.ActorRef
import com.corundumstudio.socketio.AckRequest
import org.json4s.jackson.JsonMethods

sealed abstract class IOMessage[T](event: String, actorRef: ActorRef)(
  implicit
  val t: Manifest[T]
) {
  private[socketio] def parameter(json: String): Any = {
    if (t == manifest[Unit])
      None.orNull
    else
      JsonMethods.mapper.readValue(json, t.runtimeClass)
  }
}

case class EventMessage[T](event: String, actorRef: ActorRef)(
  implicit
  t: Manifest[T]
) extends IOMessage(event, actorRef) {

  private[socketio] def copyTo(ackSender: AckRequest, json: String) = {
    EventActorMessage(actorRef, ackSender, parameter(json))
  }

}

case class BroadcastMessage[T](
  event:    String,
  actorRef: ActorRef,
  interval: Long     = -1,
  replyTo:  String
)(
  implicit
  t: Manifest[T]
) extends IOMessage(event, actorRef) {

  //  private[socketio] def copyTo(server: SocketIOServer, parameter: Any): BroadcastActorMessage = {
  //    BroadcastActorMessage(server, event, actorRef, interval, replyTo, parameter)
  //  }
}

//case class TimerMessage[T](
//  actorRef: ActorRef,
//  interval: Long = -1,
//  replyTo: String)(
//  implicit
//  t: Manifest[T]) extends IOMessage("", actorRef) {
//
//  private[socketio] def copyTo(server: SocketIOServer, parameter: Any): TimerActorMessage = {
//    TimerActorMessage(server, actorRef, interval, replyTo)
//  }
//}

private[socketio] sealed abstract class ActorMessage(actorRef: ActorRef, parameter: Any)

private[socketio] case class EventActorMessage(
  actorRef:  ActorRef,
  ackSender: AckRequest,
  parameter: Any
)
  extends ActorMessage(actorRef, parameter)

case object BroadcastRequest

case class SubscriberEvent(client: IOClient, event: String, parameter: Any)

case class AckMessage[T](t: T)(implicit val m: Manifest[T])

case class ErrorMessage(code: Int, message: String)

case class WrapperAckMessage(result: Any, error: Option[ErrorMessage] = None)
