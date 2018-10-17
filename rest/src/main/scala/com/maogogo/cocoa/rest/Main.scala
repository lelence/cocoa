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

import akka.actor.{ Actor, ActorSystem, Props, Timers }
import akka.cluster.Cluster
import akka.cluster.client.{ ClusterClient, ClusterClientSettings }
import akka.cluster.routing.{ ClusterRouterGroup, ClusterRouterGroupSettings }
import akka.cluster.singleton.{ ClusterSingletonProxy, ClusterSingletonProxySettings }
import akka.routing.{ ConsistentHashingGroup, FromConfig, RoundRobinGroup }
import com.maogogo.cocoa.common.GuiceAkka
import com.maogogo.cocoa.protobuf.data._
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import akka.util.Timeout
import com.corundumstudio.socketio.{ AckRequest, SocketIOClient }
import com.maogogo.cocoa.rest.http.HttpServer
import com.maogogo.cocoa.rest.socketio.{ DataListener, SocketIOEvent, SocketIOServer }

import scala.concurrent.Future
import scala.concurrent.duration._

object Main extends App {

  val config = ConfigFactory.parseString(
    s"""
       |akka.remote.netty.tcp.port=0
       |akka.cluster.roles=["rest"]
     """.stripMargin).withFallback(ConfigFactory.load())
  // akka.cluster.client.initial-contacts=["akka.tcp://aa@127.0.0.1:20550/system/receptionist"]
  //akka.cluster.seed-nodes=["akka.tcp://aa@127.0.0.1:20550"]
  implicit val system = ActorSystem("aa", config)

  import system.dispatcher
  // val cluster = Cluster(system)

  //  client {
  //    # Actor paths of the ClusterReceptionist actors on the servers (cluster nodes)
  //    # that the client will try to contact initially. It is mandatory to specify
  //    # at least one initial contact.
  //    # Comma separated full actor paths defined by a string on the form of
  //    # "akka.tcp://system@hostname:port/system/receptionist"
  //    initial-contacts = []

  //  val d = system.actorOf(ClusterClient.props(ClusterClientSettings(system)), "client")

  implicit val timeout = Timeout(5 seconds)

  val dd = system.actorOf(
    ClusterSingletonProxy.props(
      settings = ClusterSingletonProxySettings(system).withRole("rpc"),
      singletonManagerPath = "/user/hello"),
    name = "masterProxy")

  // val ee = d ? ClusterClient.Send("/user/hello", "hello", localAffinity = true)

  //  val cluster = Cluster(system)

  //  val c = system.actorOf(ClusterClient.props(
  //    ClusterClientSettings(system).withInitialContacts(initialContacts)), "client")
  //  c ! ClusterClient.Send("/user/serviceA", "hello", localAffinity = true)

  //  val dd = system.actorOf(FromConfig.props(), "factorialFrontend")
  //
  //  println("dd =>>" + dd)
  //  val ee: Future[Any] = dd ? "Toan"
  //  println("afwfef ==>>>" + ee)
  //
  //  ee.onComplete {
  //    case scala.util.Success(value) ⇒ println("vvvv ==>>>" + value)
  //    case scala.util.Failure(ex) ⇒ ex.printStackTrace()
  //  }

  //  val dd = system.actorOf(
  //    ClusterRouterGroup(ConsistentHashingGroup(Nil), ClusterRouterGroupSettings(
  //      totalInstances = 100, routeesPaths = List("/user/hello"),
  //      allowLocalRoutees = true)).props(),
  //    name = "workerRouter2")
  //
  //  dd ! "hahah"

  //  cluster.system.actorOf()

  new scopt.OptionParser[RestSettings]("cocoa") {
    head("cocoa", "0.0.1")

    opt[Int]('p', "port").action((x, c) => {
      c.copy(port = x)
    }).text("server port")

    opt[String]('c', "config").action((x, c) => {
      c.copy(file = x)
    }).text("config file")

  }.parse(args, RestSettings()) match {
    case Some(settings) ⇒

      val injector = GuiceAkka.withSystem(ServicesModule)
      import net.codingwell.scalaguice.InjectorExtensions._

      injector.instance[HttpServer]

      val server = new SocketIOServer()

      server.registerEvent[String]("haha") { e ⇒
        println("eee==>>>" + e.t)
        e.ackSender.sendAckData(s"hello: ${e.t}")
      }

      server.start

      println(logo)
    case None ⇒
  }

  lazy val logo =
    """
      |   _____                        _____           _
      |  / ____|                      |  __ \         | |
      | | |     ___   ___ ___   __ _  | |__) |___  ___| |_
      | | |    / _ \ / __/ _ \ / _` | |  _  // _ \/ __| __|
      | | |___| (_) | (_| (_) | (_| | | | \ \  __/\__ \ |_
      |  \_____\___/ \___\___/ \__,_| |_|  \_\___||___/\__|
      |
    """.stripMargin

}