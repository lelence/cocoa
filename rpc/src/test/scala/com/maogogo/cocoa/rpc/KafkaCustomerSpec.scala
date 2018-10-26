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

import java.util.concurrent.atomic.AtomicLong

import akka.Done
import akka.kafka.{ ConsumerSettings, Subscriptions }
import akka.kafka.scaladsl.Consumer
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.stream.scaladsl.{ Keep, Sink, Source }
import org.apache.kafka.clients.consumer.{ ConsumerConfig, ConsumerRecord }
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{ ByteArrayDeserializer, StringDeserializer }

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

class KafkaCustomerSpec extends RpcSpec {

  val config = system.settings.config.getConfig("akka.kafka.consumer")
  val consumerSettings =
    ConsumerSettings(config, new StringDeserializer, new ByteArrayDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId("group1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
      .withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000")

  "test1" in {

    //    val db = new OffsetStore
    //    val control = db.loadOffset().map { fromOffset =>
    //      Consumer
    //        .plainSource(
    //          consumerSettings,
    //          Subscriptions.assignmentWithOffset(
    //            new TopicPartition("topic1", /* partition = */ 0) -> fromOffset))
    //        .mapAsync(1)(db.businessLogicAndStoreOffset)
    //        .to(Sink.ignore)
    //        .run()
    //    }
    //
    //    class OffsetStore {
    //      private val offset = new AtomicLong
    //      def businessLogicAndStoreOffset(record: ConsumerRecord[String, Array[Byte]]): Future[Done] = {
    //        println(s"DB.save: ${record.value}")
    //        offset.set(record.offset)
    //        Future.successful(Done)
    //      }
    //      def loadOffset(): Future[Long] = Future.successful(offset.get)
    //    }

  }

  "test2" in {
    val control =
      Consumer
        .committableSource(consumerSettings, Subscriptions.topics("topic1"))
        .mapAsync(10) { msg =>
          println(msg.record.key + " ==>> " + new String(msg.record.value))
          // business(msg.record.key, msg.record.value).map(_ => msg.committableOffset)

          Future.successful(msg.committableOffset)
        }
        .mapAsync(5)(offset => offset.commitScaladsl())
        .toMat(Sink.foreach(println))(Keep.both)
        .mapMaterializedValue(DrainingControl.apply)
        .run()

    control.shutdown()

    def business(key: String, value: Array[Byte]): Future[Done] = {

      println(key + " ==>> " + new String(value))

      Future.successful(Done)
    }
  }

}
