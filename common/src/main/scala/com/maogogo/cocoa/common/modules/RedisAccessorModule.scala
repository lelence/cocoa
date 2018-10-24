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

package com.maogogo.cocoa.common.modules

import akka.actor.ActorSystem
import com.google.inject.{ AbstractModule, Provider }
import com.maogogo.cocoa.common.cache.ReidsByteStringAccessor
import javax.inject.Inject
import net.codingwell.scalaguice.ScalaModule
import redis.{ RedisClient, RedisCluster, RedisCommands, RedisServer }

object RedisAccessorModule {

  def apply(): AbstractModule with ScalaModule = {
    new AbstractModule with ScalaModule {
      override def configure(): Unit = {
        bind[ReidsByteStringAccessor].toProvider[RedisAccessorProvider].asEagerSingleton()
      }
    }
  }

  private class RedisAccessorProvider @Inject() (
    implicit
    system: ActorSystem) extends Provider[ReidsByteStringAccessor] {
    override def get(): ReidsByteStringAccessor = {
      import scala.collection.JavaConverters._

      val config = system.settings.config

      val redisSettings = config.getObjectList("redis").asScala.map(_.toConfig).map { cfg ⇒
        val host = cfg.getString("host")
        val port = cfg.getInt("port")
        (host, port)
      }

      require(redisSettings.nonEmpty, "lost redis settings")

      val redisCommand: RedisCommands = redisSettings match {
        case Seq((h, p)) ⇒ RedisClient(host = h, port = p)
        case Seq(_*) ⇒
          RedisCluster(redisSettings.map {
            case (h, p) ⇒ RedisServer(host = h, port = p)
          })
      }

      new ReidsByteStringAccessor(redisCommand)
    }
  }

}
