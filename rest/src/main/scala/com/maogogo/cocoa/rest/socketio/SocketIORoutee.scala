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

import java.util

import akka.actor.{ Actor, ActorRef }
import akka.pattern.ask
import akka.util.Timeout
import com.corundumstudio.socketio.protocol.{ Packet, PacketType }
import com.typesafe.scalalogging.LazyLogging
import org.json4s.jackson.JsonMethods

import scala.concurrent.Await
import scala.concurrent.duration._

class SocketIORoutee extends Actor with LazyLogging {

  implicit val timeout = Timeout(3 seconds)

  implicit val ex = context.system.dispatcher

  override def receive: Receive = {
    case b: BroadcastMessage[_] ⇒
      // 从client里面获取返回值, 再发出

      SocketIOClient.getClients(_ == b.event).foreach { x ⇒

        val packet: Packet = new Packet(PacketType.MESSAGE)
        packet.setSubType(PacketType.EVENT)
        packet.setName(b.replyTo)

        packet.setData(util.Arrays.asList(askActorMessage(b.actorRef, x.parameter)))

        x.client.send(packet)

      }

    case EventActorMessage(actorRef, ack, parameter) ⇒
      ack.sendAckData(askActorMessage(actorRef, parameter))
  }

  def askActorMessage(actorRef: ActorRef, parameter: Any): java.util.Map[String, Any] = {

    // 向actor发送请求获取数据并返回数据
    try {

      val _parameter = if (parameter == null) BroadcastRequest else parameter

      val fValue = (actorRef ? _parameter).map {
        case a: AckMessage[_] ⇒
          val rValue = JsonMethods.mapper.convertValue(a.t, a.m.runtimeClass)
          JsonMethods.mapper.convertValue(
            WrapperAckMessage(rValue),
            classOf[java.util.Map[String, Any]])
        case _ ⇒ throw new Exception(s"ActorRef [${actorRef.path}] return message not support")
      }

      Await.result(fValue, Duration.Inf)
    } catch {
      case t: Throwable ⇒
        logger.error(t.getMessage, t)
        JsonMethods.mapper.convertValue(
          WrapperAckMessage(None.orNull, Some(ErrorMessage(code = 500, message = t.getMessage))),
          classOf[java.util.Map[String, Any]])
    }

  }

}
