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

package com.maogogo.cocoa.rpc

import akka.actor.{ Actor, ActorSystem, Props }
import akka.stream.ActorMaterializer
import akka.testkit.{ ImplicitSender, TestKit }
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import scala.concurrent.duration._

abstract class RpcSpec extends TestKit(ActorSystem("MySpec", ConfigFactory.load())) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val mat = ActorMaterializer()

  implicit val ex = system.dispatcher

  implicit val timeout = Timeout(10 seconds)

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  // def actorRef[T <: Actor: Manifest] = system.actorOf(Props[T])

  //  import scala.reflect.ClassTag
  //  import scala.reflect._

  def actorRef[T <: Actor : Manifest](ps: Any*)(implicit m: Manifest[T]) =
    system.actorOf(Props(m.runtimeClass, ps: _*))

}
