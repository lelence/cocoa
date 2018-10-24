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

import com.maogogo.cocoa.common.modules.SysAndConfigModule
import com.maogogo.cocoa.common.{ Application, CommandSettings, GuiceAkka }
import com.typesafe.config.ConfigFactory

object Main extends Application {

  lazy val parser = (settings: CommandSettings) ⇒ {

    val seeds = settings.seeds.map(s ⇒ s""""${systemPrefix}${s.trim}"""").mkString(",")

    val config = ConfigFactory
      .parseString(
        s"""
           |akka.remote.netty.tcp.port=${settings.port}
           |akka.remote.netty.tcp.hostname="127.0.0.1"
           |akka.cluster.seed-nodes=[$seeds]
        """.stripMargin)
      .withFallback(ConfigFactory.load())

    GuiceAkka.withCluster(config, ServicesModule)

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
