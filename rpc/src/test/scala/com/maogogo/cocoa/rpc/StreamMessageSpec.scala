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

import java.nio.file.Paths

import akka.actor.Status.Success
import akka.{ Done, NotUsed }
import akka.actor.{ Actor, ActorRef, PoisonPill, Props }
import akka.stream.{ IOResult, OverflowStrategy, SubstreamCancelStrategy }
import akka.stream.scaladsl.{ FileIO, Flow, Keep, Sink, Source }
import akka.testkit.TestProbe
import akka.util.ByteString
import com.typesafe.scalalogging.LazyLogging
import akka.pattern.ask

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.util.Random

class StreamMessageSpec extends RpcSpec {

  // https://github.com/animeshinvinci/akka-kafka-streams/blob/de05ac756901579a0f3efbaa1d5143b3a1933e27/src/main/scala/MyFirstStream.scala
  // https://github.com/asura-pro/indigo-api/blob/91adcfa2d6cdf080bd15a47dd51b35780cbe562a/asura-core/src/main/scala/asura/core/actor/flow/WebSocketMessageHandler.scala

  "test1" in {

    val source: Source[Int, NotUsed] = Source(1 to 5)

    source.runForeach(println)

    val sink: Sink[Any, Future[Done]] = Sink.foreach(println)
    source.runWith(sink)

    source.to(sink).run()

    source.toMat(sink)(Keep.left).run()

    //    Flow[String].toMat()
  }

  "test2" in {

    val source: Source[Int, NotUsed] = Source(1 to 10)

    val sink = Sink.fold[Int, Int](0)(_ + _)

    val ss: NotUsed = source.to(sink).run()

    val qq: NotUsed = source.toMat(sink)(Keep.left).run()

  }

  "test3" in {

    val dd: Sink[Int, NotUsed] = Flow[Int].map(_ * 2).to(Sink.foreach(println))

    val ss: Sink[Int, Future[Done]] = Flow[Int].map(_ * 2).toMat(Sink.foreach(println))(Keep.right)

    Source(1 to 5).runWith(dd)

    Source(1 to 5).runWith(ss)

    val otherSink: Sink[Int, NotUsed] =
      Flow[Int].alsoTo(Sink.foreach(println)).to(Sink.foreach(println))

    Source(1 to 6).to(otherSink).run()

  }

  "test4" in {
    val matValuePoweredSource =
      Source.actorRef[String](bufferSize = 100, overflowStrategy = OverflowStrategy.fail)

    val (actorRef, source) = matValuePoweredSource.preMaterialize()

    actorRef ! "Hello!"
    actorRef ! "Hello!"
    actorRef ! "Hello!"
    actorRef ! "Hello!"
    actorRef ! "Hello!"
    actorRef ! "Hello!"
    actorRef ! "Hello!"

    source.runWith(Sink.foreach(println))
  }

  "test5" in {

    val words: Source[String, NotUsed] =
      Source(List("hello", "hi"))

    val AckMessage = AckingReceiver.Ack

    val InitMessage = AckingReceiver.StreamInitialized
    val OnCompleteMessage = AckingReceiver.StreamCompleted
    val onErrorMessage = (ex: Throwable) ⇒ AckingReceiver.StreamFailure(ex)

    val probe = TestProbe()

    val receiver = actorRef[Worker](probe.ref)

    val sink = Sink.actorRefWithAck(
      receiver,
      onInitMessage = InitMessage,
      ackMessage = AckMessage,
      onCompleteMessage = OnCompleteMessage,
      onFailureMessage = onErrorMessage)

    words
      .map(_.toLowerCase)
      .runWith(sink)

    probe.expectMsg("Stream initialized!")
    probe.expectMsg("hello")
    probe.expectMsg("hi")
    probe.expectMsg("Stream completed!")

  }

  "test6" in {
    val source = Source.actorRef[String](10, OverflowStrategy.dropHead)
    val (aa, ss) = source.preMaterialize()

    val probe = TestProbe()

    val worker = actorRef[Worker](probe.ref)

    val sink = Sink.actorRefWithAck(
      worker,
      AckingReceiver.StreamInitialized,
      AckingReceiver.Ack,
      AckingReceiver.StreamCompleted)

    ss.runWith(sink)

    (aa ? "hello").foreach(println)
    aa ! "hi"

    probe.expectMsg("Stream initialized!")
    probe.expectMsg("hello")
    probe.expectMsg("hi")
    //probe.expectMsg("Stream completed!")

  }

  "test7" in {

    val source: Source[Double, ActorRef] =
    // will not fail of the buffer size is 0 = no buffer -> no overflow -> no failure
      Source.actorRef[String](1, OverflowStrategy.fail).map(_ => Random.nextDouble())

    val sink: Sink[Double, Future[Done]] = Sink.foreach { e =>
      println(s"start $e")
      Thread.sleep(100)
      println(s"done $e")
    }
    val actorRef: ActorRef = Flow[Double].to(sink).runWith(source)

    system.scheduler.schedule(0.micro, 1.milli, actorRef, "tick")
    system.scheduler.scheduleOnce(500.milli, actorRef, PoisonPill)
    Thread.sleep(1000)
    println("done")

  }

  "test8" in {

    val source = Source.actorRef[String](10, OverflowStrategy.fail)

    val probe = TestProbe()

    val worker = actorRef[Worker](probe.ref)

    val sink = Sink.actorRef(worker, ())

    val dd: ActorRef = Flow[String].to(sink).runWith(source)

    val ee = (dd ? "Hello").mapTo[AckingReceiver.Ack]

    val ff = Await.result(ee, 3 seconds)

    println(ff)

    assert(ff.s == "hi:Hello")

    //    ee onComplete {
    //      case util.Success(value) ⇒ println("===>>>>" + value)
    //      case scala.util.Failure(exception) ⇒ exception.printStackTrace()
    //    }
  }

  "test9" in {

    // continue processing of the replies from the actor
    //      .map(_.toLowerCase)
    //      .runWith(Sink.ignore)

    // https://doc.akka.io/docs/akka/2.5.4/scala/stream/stream-integrations.html#mapasync-ask
    // https://doc.akka.io/docs/akka/2.5/stream/stream-testkit.html#testkit

    val sinkUnderTest = Flow[Int].map(_.toString).toMat(Sink.fold("")(_ + _))(Keep.right)

    val (ref, future) = Source.actorRef(8, OverflowStrategy.fail)
      .toMat(sinkUnderTest)(Keep.both).run()

    // ref ! akka.actor.Status.Success(())

    ref ! akka.actor.Status.Success(())

    val result = Await.result(future, 3.seconds)
    println("===>>>" + result)
    assert(result == "123")

  }

  "test10" in {
    val outgoingMessages: Source[Double, NotUsed] =
      Source.actorRef[Double](10, OverflowStrategy.dropHead)
        .mapMaterializedValue { outActor =>
          // workActor ! SenderMessage(outActor)
          NotUsed
        }
        .map(result => Random.nextDouble())
        .keepAlive(10 seconds, () => 0)
    // Flow[String].toMat()

  }

}

object AckingReceiver {

  case class Ack(s: String = "")

  case object StreamInitialized

  case object StreamCompleted

  final case class StreamFailure(ex: Throwable)

}

class Worker(probe: ActorRef) extends Actor with LazyLogging {

  import AckingReceiver._

  override def receive: Receive = {
    case StreamInitialized ⇒
      println("===>>> Stream initialized!")
      probe ! "Stream initialized!"
      sender() ! Ack // ack to allow the stream to proceed sending more elements

    case el: String ⇒
      println("----->>> Received element: {}", el)
      // probe ! el
      sender() ! s"hi:$el" // ack to allow the stream to proceed sending more elements

    case StreamCompleted ⇒
      println("===>>> Stream completed!")
      probe ! "Stream completed!"
    case StreamFailure(ex) ⇒
      logger.error("Stream failed!", ex)
  }

}
