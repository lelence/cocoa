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

import akka.actor.ActorSystem
import akka.protobuf.ByteString
import com.google.common.hash.Hashing
import com.maogogo.cocoa.protobuf.data.RestSettings
import redis.{ RedisClient, RedisCluster, RedisCommands, RedisServer }

import scala.util.Either

object Test extends App {

  //  implicit val system = ActorSystem("")
  //
  //  def aa(s: String*): Either[RedisClient, RedisCluster] = {
  //     s.length match {
  //      case 1 ⇒ Left(RedisClient(host = "0.0.0.0"))
  //      case _ ⇒ Right(RedisCluster(Seq.empty[RedisServer]))
  //    }
  //  }
  //
  //  aa("a")

  //  def divideXByY(x: Int, y: Int): Either[String, Int] = {
  //    if (y == 0) Left("Dude, can't divide by 0")
  //    else {
  //      val dd: Either[Nothing, Int] = Right(x / y)
  //
  //      dd
  //    }
  //  }

  val dd: scala.collection.mutable.Seq[(String, Int)] = scala.collection.mutable.Seq(("aa", 11))

  // dd.head

  dd match {
    case head +: Seq() ⇒ println("aaa")
    case head +: tail ⇒ println("dddd")
  }

}

