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

package com.maogogo.cocoa.rpc.services

import akka.actor.Actor

class TestActor extends Actor {

  //  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute, false) {
  //    case _: ArithmeticException ⇒ Resume
  //    case _: TestException ⇒ Restart
  //    case _: IllegalArgumentException ⇒ Stop
  //    case _: Exception ⇒ Escalate
  //  }
  //
  //  override def preStart(): Unit = {
  //    println("h====>>>")
  //  }
  //
  //  override def receive: Receive = akka.event.LoggingReceive {
  //    case s: String if s != "restart" ⇒
  //      println("====>>>>>>>>>>>>>>>>hello : " + s)
  //      sender() ! "hello : " + s
  //
  //    case s: String if s == "restart" ⇒
  //      throw new TestException("restart")
  //  }
  override def receive: Receive = {
    case s: String ⇒
      println("ssssss===>>" + s)
  }
}
