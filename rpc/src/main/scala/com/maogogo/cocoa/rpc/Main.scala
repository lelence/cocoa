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

import akka.actor.ActorRef
import akka.cluster.Cluster
import akka.util.Timeout
import com.google.inject.name.Names
import com.maogogo.cocoa.common.actor.ActorRestartAction
import com.maogogo.cocoa.common.cluster.AA
import com.maogogo.cocoa.common.{ Application, CommandSettings, Constants, GuiceAkka }
import com.maogogo.cocoa.rpc.node.NodeHttpServer
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

object Main extends Application {

  implicit val timeout = Timeout(3 seconds)

  lazy val parser = (settings: CommandSettings) ⇒ {

    val seeds = settings.seeds.map(s ⇒ s""""${systemPrefix}${s.trim}"""").mkString(",")

    val roles = settings.roles.mkString(", ")

    val info =
      s"""
         |akka.remote.netty.tcp.port=${settings.port}
         |akka.remote.netty.tcp.hostname="127.0.0.1"
         |akka.cluster.seed-nodes=[${seeds}]
         |akka.cluster.roles=[${roles}]
        """.stripMargin

    val config = ConfigFactory
      .parseString(info)
      .withFallback(ConfigFactory.load())

    val injector = GuiceAkka(true).config(config).modules(new ServicesModule).injector()

    import net.codingwell.scalaguice.InjectorExtensions._

    val dd = injector.instance[Map[String, Option[ActorRef]], AA]

    // Names.named(Constants.cluster_actor_map)

    println(s"""${"=" * 50}${info}\n${"=" * 50}""")

    dd.map { k ⇒
      println(k)
    }

    //    val cluster = injector.instance[Cluster]
    //
    //    injector.instance[NodeHttpServer]

    //    cluster.registerOnMemberUp {
    //
    //      Thread.sleep(5 * 1000)
    //
    //      val dd = cluster.system.actorSelection("/user/HelloActor/*")
    //
    //      println(dd.pathString)
    //
    //      // dd ! 1000
    //
    //      Thread.sleep(5 * 1000)
    //
    //      dd ! "hahhaha"
    //
    //      dd ! ActorRestartAction
    //
    //      // dd ! new Exception("restart exception")
    //
    //      dd ! "uuuuuhhhhh"
    //    }

    //    map.foreach {
    //      case (k, ref) ⇒
    //
    //        Thread.sleep(2000)
    //
    //        println("kk =>>" + k + "####" + ref.path)
    //
    //        ref ? "ddddd"
    //
    //    }
    // map.get("HelloActor").get ! 1000

  }

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
