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

import akka.actor.Actor

import scala.concurrent.duration._

class SocketIOServerActor extends Actor {

  implicit val ex = context.system.dispatcher

  override def receive: Receive = {
    case StartBroadcast ⇒
      import SocketIOServerLocal._

      getProviders.map { p ⇒
        // 去掉不需要广播的消息
        val ms = p.methods.filterNot(_.event.broadcast == 0)
        p.copy(methods = ms)
      }.map { p ⇒
        p.methods.map { m ⇒
          // 定时调用并推送消息
          // 定时广播给指定客户端
          // TODO(Toan)这里还缺少一个广播给所有
          context.system.scheduler.schedule(5 seconds, m.event.interval seconds, new Runnable {
            override def run(): Unit = {
              getSubscriber(m.event.event).map { suber ⇒
                val resp = EventReflection.invokeMethod(p.instance, m, suber.json)
                suber.client.sendEvent(m.event.event, resp)
              }
            }
          })
        }
      }
  }

}

