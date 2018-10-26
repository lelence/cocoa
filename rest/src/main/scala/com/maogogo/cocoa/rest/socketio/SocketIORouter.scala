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

import akka.actor.{ Actor, Props }
import akka.routing.RoundRobinPool

import scala.concurrent.duration._
import scala.util.Random

class SocketIORouter(pool: Int) extends Actor {

  // val routee = context.system.actorOf(Props[SocketIORoutee])

  implicit val ex = context.system.dispatcher

  lazy val replyRoutor = context.actorOf(
    RoundRobinPool(10).props(Props[SocketIORoutee]),
    s"socketio_routee")

  override def receive: Receive = {
    case b: BroadcastMessage[_] ⇒
      // 从client里面获取返回值, 再发出

      b.interval match {
        case i if i < 1 ⇒ replyRoutor forward b // 这里只转发消息
        case _ ⇒

          // 订阅消息需要定时发送
          val broadcastRoutee = context.actorOf(
            RoundRobinPool(10).props(Props[SocketIORoutee]),
            s"socket_timer_routee_${Random.nextInt()}")

          context.system.scheduler.schedule(2 seconds, b.interval seconds, broadcastRoutee, b)
      }

    case e: EventActorMessage ⇒
      // 获取返回值
      // 这里直接转发消息
      replyRoutor.forward(e)

  }
}
