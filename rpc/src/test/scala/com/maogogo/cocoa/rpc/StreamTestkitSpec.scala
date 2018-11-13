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

import akka.NotUsed
import akka.stream.scaladsl.{ Flow, Keep, Sink, Source }
import akka.testkit.TestProbe

import scala.collection.immutable
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

class StreamTestkitSpec extends RpcSpec {

  // testOnly *StreamTestkitSpec -- -z test1
  "test1" in {

    val sinkUnderTest = Flow[Int].map(_ * 2).toMat(Sink.fold(0)(_ + _))(Keep.right)

    val future = Source(1 to 4).runWith(sinkUnderTest)
    val result = Await.result(future, 3.seconds)

    assert(result == 20)

  }

  "test2" in {

    val s = Source.repeat(1).map(_ * 2)

    val dd: Sink[String, Future[immutable.Seq[String]]] = Sink.seq[String]

    val f = s.take(10).runWith(Sink.seq)

    val result = Await.result(f, 3.seconds)

    assert(result == Seq.fill(10)(2))

  }

  "test3" in {
    val flowUnderTest: Flow[Int, Int, NotUsed] = Flow[Int].takeWhile(_ < 5)

    val future = Source(1 to 10).via(flowUnderTest).runWith(Sink.fold(Seq.empty[Int])(_ :+ _))

    val result = Await.result(future, 3.seconds)

    assert(result == (1 to 4))

  }

  "test4" in {
    import akka.pattern.pipe

    val sourceUnderTest = Source(1 to 4).grouped(2)

    val probe = TestProbe()
    sourceUnderTest.runWith(Sink.seq).pipeTo(probe.ref)
    probe.expectMsg(3.seconds, Seq(Seq(1, 2), Seq(3, 4)))

  }
}
