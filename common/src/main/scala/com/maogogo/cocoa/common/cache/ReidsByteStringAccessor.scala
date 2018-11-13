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

import akka.util.ByteString
import redis.RedisCommands

import scala.concurrent.Future

class ReidsByteStringAccessor(redis: RedisCommands) extends Accessor[String, ByteString] {

  override def get(ks: String): Future[Option[ByteString]] = redis.get(ks)

  override def set(ks: String, vs: ByteString, ttl: Option[Long] = None): Future[Boolean] = redis.set(ks, vs, exSeconds = ttl)

  override def push(ks: String, vss: Seq[ByteString]): Future[Long] = redis.lpush(ks, vss: _*)

  override def pull(ks: String, start: Long, stop: Long): Future[Seq[ByteString]] = redis.lrange(ks, start, stop)
}
