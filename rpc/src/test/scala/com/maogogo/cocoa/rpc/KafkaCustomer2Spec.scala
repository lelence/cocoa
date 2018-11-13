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

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.{ ConsumerSettings, Subscriptions }
import akka.kafka.scaladsl.Consumer
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Keep, Sink }
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.scalatest.WordSpec

import scala.concurrent.Future

object KafkaCustomer2Spec extends App {

  implicit val system = ActorSystem("MySpec", ConfigFactory.load())

  implicit val mat = ActorMaterializer()

  implicit val ex = system.dispatcher

  val config = system.settings.config.getConfig("akka.kafka.consumer")
  val consumerSettings =
    ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId("group1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  //      .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
  //      .withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000")

  val control =
    Consumer.committableSource(consumerSettings, Subscriptions.topics("topic2"))
      .mapAsync(10) { msg =>

        business(msg.record.key(), msg.record.value())
        // println("consumer msg => " + msg.record.key() + "###" + msg.record.value())
        // msg.committableOffset.commitScaladsl()
        // Future.successful(Done)
      }.runWith(Sink.ignore)

  def business(key: String, value: String): Future[Done] = {

    println(key + " ===> " + value)

    Future.successful(Done)
  }

  //   control.shutdown()

}
