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

import java.util

import com.corundumstudio.socketio.SocketIOClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.maogogo.cocoa.common._
import scalapb.json4s.JsonFormat

package object socketio {

  //  implicit def string2ProtoBuf[T <: ProtoBuf[T]](json: String)(
  //    implicit
  //    c: scalapb.GeneratedMessageCompanion[T]): T =
  //    JsonFormat.fromJsonString[T](json)
  //
  //  implicit def protoBuf2JavaMap[T <: ProtoBuf[T]](t: T): java.util.Map[String, Any] = {
  //    val mapper = new ObjectMapper()
  //    mapper.readValue(JsonFormat.toJsonString(t), classOf[java.util.Map[String, Any]])
  //  }

  // broadcat => 0: 不广播, 1: 广播订阅者, 2: 广播所有
  case class event(event: String, broadcast: Int, interval: Long, replyTo: String) extends scala.annotation.StaticAnnotation

  import scala.reflect.runtime.universe._

  // 消息class
  case class ProviderEventClass[T](instance: T, clazz: Class[T], methods: Seq[ProviderEventMethod])

  // 消息方法
  case class ProviderEventMethod(event: event, method: MethodSymbol,
    paramClazz: Class[_], futureType: Type)

  // 客户端订阅的消息
  case class SubscriberEvent(client: SocketIOClient, event: String, json: String)

  case object StartBroadcast

  // case class BroadMessage[T](server: SocketIOServer, i: T, t: Manifest[T], r: EventReflectMethod)

  //  case class BroadcatReflectMethod(interval: Int, anno: Option[String],
  //    method: MethodSymbol, paramType: Type, futureType: Type) extends ReflectMethod

}
