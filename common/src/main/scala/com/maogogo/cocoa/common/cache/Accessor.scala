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
import com.maogogo.cocoa.common._

import scala.concurrent.Future

trait Accessor[KS, VS] {

  def get(ks: KS): Future[Option[VS]]

  def set(ks: KS, vs: VS, ttl: Option[Long]): Future[Boolean]

  def push(ks: KS, vss: Seq[VS]): Future[Long]

  def pull(ks: KS, start: Long = 0, stop: Long = -1): Future[Seq[VS]]
}

final class StringSerializer extends KeySerializer[String, String] {
  override def serialize(k: String): String = identity(k)
}

final class ByteStringSerializer extends KeySerializer[String, ByteString] {
  override def serialize(k: String): ByteString = ByteString(k)
}

final class ProtoBufByteStringSerializer[T <: ProtoBuf[T]] extends KeySerializer[T, ByteString] {
  override def serialize(k: T): ByteString = ByteString.fromArray(k.toByteArray)
}

final class ProtoBufByteStringDeserializer[T <: ProtoBuf[T]](
  implicit
  c: scalapb.GeneratedMessageCompanion[T]) extends ValueSerializer[ByteString, T] {

  override def serialize(v: ByteString): T = c.parseFrom(v.toArray)

  override def deserialize(vs: T): ByteString = ByteString.fromArray(vs.toByteArray)
}

//
//final class ProtoBufStringSerializer[T <: ProtoBuf[T]] extends KeySerializer[T, String] {
//  override def serialize(k: T): String = {
//    val bytes = Hashing.murmur3_128().hashBytes(k.toByteArray).asBytes
//    new String(bytes)
//  }
//}
