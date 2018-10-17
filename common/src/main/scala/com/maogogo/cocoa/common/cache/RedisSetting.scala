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

package com.maogogo.cocoa.common.cache

import akka.actor.ActorSystem
import com.typesafe.config.Config
import redis.{ RedisClient, RedisCluster, RedisServer }

object RedisSetting {
  def apply(config: Config)(implicit system: ActorSystem): RedisSetting = new RedisSetting(config)
}

class RedisSetting(config: Config)(implicit system: ActorSystem) {

  def register: Either[RedisClient, RedisCluster] = {

    import scala.collection.JavaConverters._

    val redisSettings = config.getObjectList("redis").asScala.map(_.toConfig).map { cfg ⇒
      val host = cfg.getString("host")
      val port = cfg.getInt("port")
      (host, port)
    }

    redisSettings match {
      case head +: Seq() ⇒
        val client = RedisClient(host = head._1, port = head._2)
        Left(client)
      case head +: tail ⇒
        val cluster = RedisCluster(redisSettings.map { list ⇒
          RedisServer(host = list._1, port = list._2)
        })
        Right(cluster)
    }
  }

}
