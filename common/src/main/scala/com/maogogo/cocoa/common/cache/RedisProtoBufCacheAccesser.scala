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

import com.maogogo.cocoa.common._
import redis.{ RedisClient, RedisCluster, RedisCommands }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RedisProtoBufCacheAccesser[T <: ProtoBuf[T]](redis: Either[RedisClient, RedisCluster])(
  implicit
  c: scalapb.GeneratedMessageCompanion[T]) extends Accessor[String, T] {

  val vsDeserializer = new ProtoBufByteStringDeserializer[T]

  override def get(ks: String): Future[Option[T]] =
    redisClient.get(ks).map(_.map(vsDeserializer.serialize))

  override def set(ks: String, vs: T, ttl: Option[Long] = None): Future[Boolean] =
    redisClient.set(ks, vsDeserializer.deserialize(vs), exSeconds = ttl)

  override def push(ks: String, vss: Seq[T]): Future[Long] =
    redisClient.lpush(ks, vss.map(vsDeserializer.deserialize): _*)

  override def pull(ks: String, start: Long = 0, stop: Long = -1): Future[Seq[T]] =
    redisClient.lrange(ks, start, stop).map(_.map(vsDeserializer.serialize))

  private[cache] lazy val redisClient: RedisCommands =
    if (redis.isLeft) redis.left.get else redis.right.get

}
