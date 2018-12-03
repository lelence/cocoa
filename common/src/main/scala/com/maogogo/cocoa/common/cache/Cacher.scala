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

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

trait Cacher[K, KS, V, VS] {

  val ks      : KeySerializer[K, KS]
  val vs      : ValueSerializer[V, VS]
  val accessor: Accessor[KS, VS]

  implicit def toKeySerializer(k: K): KS = ks.serialize(k)

  implicit def toValueDeserializer(
    vsFuture: Future[Option[VS]]
  ): Future[Option[V]] = vsFuture.map(_.map(vs.deserialize))

  implicit def toValueDeserializers(vsFuture: Future[Seq[VS]]): Future[Seq[V]] =
    vsFuture.map(_.map(vs.deserialize))

  implicit def toValueSerializer(v: V): VS = vs.serialize(v)

  implicit def toValueSerializers(v: Seq[V]): Seq[VS] = v.map(toValueSerializer)

  def get(k: K): Future[Option[V]] = accessor.get(k)

  def getOrElse(k: K, ttl: Option[Long] = None)(fallback: ⇒ Future[Option[V]]): Future[Option[V]] = {
    for {
      vOption ← get(k)
      vMissed = vOption.isEmpty
      fallbackOption ← if (vMissed) fallback else Future.successful(vOption)
      _ ← if (vMissed && fallbackOption.isDefined) put(k, fallbackOption.get, ttl) else Future.unit
    } yield fallbackOption
  }

  def put(k: K, v: V, ttl: Option[Long] = None): Future[Boolean] = accessor.set(k, v, ttl)

  def push(k: K, vs: Seq[V]): Future[Long] = accessor.push(k, vs)

  def pull(k: K, start: Long = 0, stop: Long = -1): Future[Seq[V]] = accessor.pull(k, start, stop)
}

trait KeySerializer[K, KS] {
  def serialize(k: K): KS
}

trait ValueSerializer[V, VS] {

  def serialize(v: V): VS

  def deserialize(vs: VS): V

}

