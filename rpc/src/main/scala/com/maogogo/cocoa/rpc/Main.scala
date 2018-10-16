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

import java.net.InetAddress

import com.maogogo.cocoa.common.inject._
import akka.actor.{ ActorSystem, PoisonPill, Props }
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.routing.{ ClusterRouterGroup, ClusterRouterGroupSettings }
import akka.cluster.singleton.{ ClusterSingletonManager, ClusterSingletonManagerSettings }
import akka.routing.RoundRobinGroup
import akka.stream.ActorMaterializer
import com.google.inject.Guice
import com.maogogo.cocoa.common.GuiceAkka
import com.maogogo.cocoa.common.modules.SysAndConfigModule
import com.maogogo.cocoa.protobuf.data.{ RestSettings, RpcSettings }
import com.maogogo.cocoa.rpc.services.HelloActor
import com.typesafe.config.ConfigFactory

object Main extends App {

  //  val config = ConfigFactory.parseString(
  //    s"""
  //       |akka.remote.netty.tcp.port=20550
  //       |akka.remote.netty.tcp.hostname="127.0.0.1"
  //       |akka.cluster.roles=["rpc"]
  //       |akka.cluster.seed-nodes=["akka.tcp://aa@127.0.0.1:20550"]
  //     """.stripMargin).withFallback(ConfigFactory.load())
  //
  //  println(config)
  //
  implicit val system = ActorSystem("aa", ConfigFactory.load())
  //  // val cluster = Cluster(system)

  //  val hello = new HelloActor("aaaa")

  //  val ss = Props(new HelloActor("aaaa"))
  //
  //  val qq = Props(new HelloActor("aaaa"))
  //  println(ss)
  //  println(qq)
  //
  //  val dd = system.actorOf(ss, "hello")

  //  val ee = system.actorOf(ss, "hello")

  //  println(dd)

  //  dd ! "efwfe"
  //  println(ee)

  //
  //  system.actorOf(
  //    ClusterSingletonManager.props(
  //      singletonProps = Props[HelloActor],
  //      terminationMessage = PoisonPill,
  //      settings = ClusterSingletonManagerSettings(system).withRole("rpc")), "hello")

  // ClusterClientReceptionist(system).registerService(dd)

  //  new scopt.OptionParser[RpcSettings]("cocoa") {
  //    head("cocoa", "0.0.1")
  //
  //    opt[Int]('p', "port").action((x, c) => {
  //      c.copy(port = x)
  //    }).text("server port")
  //
  //    opt[Seq[String]]('s', "seeds").action((x, c) => {
  //      c.copy(seeds = x)
  //    }).text("server seeds")
  //
  //  }.parse(args, RpcSettings()) match {
  //    case Some(settings) ⇒
  //
  //      val seeds = settings.seeds.map(s ⇒ s""""akka.tcp://MyClusterSystem@${s.trim}"""").mkString(",")
  //
  //      val config = ConfigFactory
  //        .parseString(
  //          s"""
  //              akka.remote.netty.tcp.port=${settings.port}
  //              akka.remote.netty.tcp.hostname="127.0.0.1"
  //              akka.cluster.seed-nodes=[$seeds]
  //              """)
  //        .withFallback(ConfigFactory.load())
  //
  //      val injector = GuiceAkka(config, cluster = Some(true), ServicesModule)
  //      //      implicit val system = injector.
  //      // import com.maogogo.cocoa.common.inject._
  //      // implicit val system = injector.actorSystem
  //
  //      import net.codingwell.scalaguice.InjectorExtensions._
  ////       injector.
  //
  ////       val dd = injectActor[HelloActor] required
  //
  ////      ClusterClientReceptionist(system).registerService(dd)
  //      // dd ! "hahha"
  //
  //      println(logo)
  //    case None ⇒
  //  }

  lazy val logo =
    """
      |   _____                        _____
      |  / ____|                      |  __ \
      | | |     ___   ___ ___   __ _  | |__) |_ __   ___
      | | |    / _ \ / __/ _ \ / _` | |  _  /| '_ \ / __|
      | | |___| (_) | (_| (_) | (_| | | | \ \| |_) | (__
      |  \_____\___/ \___\___/ \__,_| |_|  \_\ .__/ \___|
      |                                      | |
      |                                      |_|
    """.stripMargin

}
