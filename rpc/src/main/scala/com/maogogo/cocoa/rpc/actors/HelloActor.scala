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

package com.maogogo.cocoa.rpc.actors

import akka.actor.SupervisorStrategy._
import akka.actor.{ Actor, OneForOneStrategy }
import com.maogogo.cocoa.common.actor.NodeActor
import com.maogogo.cocoa.rpc._

class HelloActor extends Actor with NodeActor {

  override def preStart(): Unit = {
    println("===restart>>" + self.path)
  }

  import com.github.nscala_time.time.Imports._

  override def doReceive: Receive = {
    case s: String ⇒
      println("===>>>>" + self.path)
      println(DateTime.now().toString + " ==>>" + s)
  }

}
