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

import akka.actor.ActorRef
import com.corundumstudio.socketio.listener.DataListener
import com.corundumstudio.socketio.{ AckRequest, SocketIOClient }
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.google.inject.Injector
import com.maogogo.cocoa.rest.socketio.EventReflection._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

private[socketio] object SocketIOServerLocal {

  //
  private val eventProvider: collection.mutable.Seq[ProviderEventClass[_]] = collection.mutable.Seq.empty

  private val eventSubscriber: collection.mutable.Set[SubscriberEvent] = collection.mutable.Set.empty

  lazy val mapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper
  }

  def addSubscriber(s: SubscriberEvent): Unit = eventSubscriber + s

  def getSubscriber(event: String): Seq[SubscriberEvent] =
    eventSubscriber.filter(_.event == event).toSeq

  def getSubscribers: Seq[SubscriberEvent] = eventSubscriber.toSeq

  def addProvider[T](t: ProviderEventClass[T]): Unit = eventProvider :+ t

  def getProviders: Seq[ProviderEventClass[_]] = eventProvider

  def getProvider(name: String): Seq[ProviderEventClass[_]] = {

    eventProvider.map { e ⇒
      // TODO(Toan) 这里会不会有同一个类多个方法注册了相同的事件
      // 相同事件 应该只有一个
      val ms = e.methods.find {
        _.event.event == name
      } match {
        case Some(m) ⇒ Seq(m)
        case _ ⇒ Seq.empty
      }
      e.copy(methods = ms)
    }.filter(_.methods.nonEmpty) // .headOption
  }
}

class SocketIOServer(
  injector: Injector,
  server: com.corundumstudio.socketio.SocketIOServer,
  router: ActorRef) extends LazyLogging {

  import SocketIOServerLocal._

  // 这里分两种情况 1. 直接注册listener, 2. 需要注入到actor
  def register[T: Manifest](implicit m: Manifest[T]): Unit = {

    val instance = injector.getInstance(m.runtimeClass)

    val methods = EventReflection.lookupEventMethods

    val t = ProviderEventClass(instance = instance, clazz = m.runtimeClass, methods = methods)

    addProvider(t)
  }

  def start: Unit = {

    // 注册请求事件
    server.addEventListener("", classOf[java.util.Map[String, Any]], new DataListener[java.util.Map[String, Any]] {
      override def onData(client: SocketIOClient, data: util.Map[String, Any], ackSender: AckRequest): Unit = {
        val event = data.get("method").toString

        val json = mapper.writeValueAsString(data.get("params"))

        addSubscriber(SubscriberEvent(client, event, json))

        getProvider(event).foreach { clazz ⇒

          val methodEvent = clazz.methods.head

          // TODO(Toan) 这里缺少了一种情况
          // 客户端直接请求数据
          // 客户端请求广播数据
          // 客户端订阅广播数据
          // 主动广播消息
          methodEvent.event match {
            case event(_, 0, _, _) ⇒
              val methodEvent = clazz.methods.head
              val instance = clazz.instance
              val resp = EventReflection.invokeMethod(instance, methodEvent, json)
              ackSender.sendAckData(resp)
              // replyMessage(ackSender, clazz, json)
            case event(_, 1, -1, _) ⇒ // broadMessage()
            case event(_, 2, _, _) ⇒ logger.info("auto reply client message by actor")
            case _ ⇒ throw new Exception("not supported")
          }
        }
      }
    })

    server.start()
  }

  def replyMessage(ackSender: AckRequest, clazz: ProviderEventClass[_], json: String): Unit = {

    val methodEvent = clazz.methods.head

    val instance = clazz.instance

    val resp = EventReflection.invokeMethod(instance, methodEvent, json)

    ackSender.sendAckData(resp)

  }

  def broadMessage(event: String, anyRef: AnyRef): Unit = {
    server.getBroadcastOperations.sendEvent(event, anyRef)
  }

  def stop: Unit = server.stop()

  def sendErrorMessage(ackSender: AckRequest): Unit = {
    ackSender.sendAckData("error")
  }

}
