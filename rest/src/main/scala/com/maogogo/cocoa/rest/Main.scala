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

import akka.actor.ActorRef
import com.google.inject.Provider
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.maogogo.cocoa.common.cluster.ProxyActor
import com.maogogo.cocoa.common.modules.ClusterProxyModule
import com.maogogo.cocoa.common.{ Application, CommandSettings, GuiceAkka }
import com.maogogo.cocoa.rest.http.HttpServer
import com.typesafe.config.ConfigFactory

object Main extends Application {

  override val parser = (settings: CommandSettings) ⇒ {

    val seeds = settings.seeds.map(s ⇒ s""""${systemPrefix}${s.trim}"""").mkString(",")

    val config = ConfigFactory
      .parseString(
        s"""
           |akka.remote.netty.tcp.port=${settings.port}
           |akka.remote.netty.tcp.hostname="127.0.0.1"
           |akka.cluster.seed-nodes=["akka.tcp://MyClusterSystem@127.0.0.1:2555"]
           """.stripMargin)
      .withFallback(ConfigFactory.load())

    //    val uu = new FactoryModuleBuilder()
    //      .implement(classOf[Provider[ActorRef]], classOf[ProxyActorProvider]).build(classOf[ProxyActorFactory])

    val injector = GuiceAkka(true).config(config).modules(ServicesModule, ClusterProxyModule()).injector()

    import net.codingwell.scalaguice.InjectorExtensions._

    //
    injector.instance[HttpServer]

    val dd = injector.getInstance(classOf[ProxyActor])

    println("ddd 11===>>" + dd.ref("HelloActor").get)

    println("ddd 22===>>" + dd.ref("HelloActor").get)

    // println("ddd ==>>" + dd)
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