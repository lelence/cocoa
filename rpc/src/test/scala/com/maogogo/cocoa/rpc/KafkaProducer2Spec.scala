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

import akka.{ Done, NotUsed }
import akka.actor.ActorSystem
import akka.kafka.ProducerMessage.MultiResultPart
import akka.kafka.{ ProducerMessage, ProducerSettings }
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Flow, Sink, Source }
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.Future

object KafkaProducer2Spec extends App {

  implicit val system = ActorSystem("MySpec", ConfigFactory.load())

  implicit val mat = ActorMaterializer()

  implicit val ex = system.dispatcher

  val config = system.settings.config.getConfig("akka.kafka.producer")
  val producerSettings =
    ProducerSettings(config, new StringSerializer, new StringSerializer)
      .withBootstrapServers("localhost:9092")

  // new ProducerRecord[String, String]("topic1", value)

  // val dd: Flow[ProducerMessage.Envelope[String, String, Nothing], ProducerMessage.Results[String, String, Nothing], NotUsed] = Producer.flexiFlow(producerSettings)
  val done: Future[Done] =
    Source(1 to 100)
      .map(_.toString)
      .map(value => new ProducerRecord("topic2", 0, "key" + value, value))
      .runWith(Producer.plainSink(producerSettings))

  done onComplete {
    case scala.util.Success(value) ⇒ println(value)
    case scala.util.Failure(exception) ⇒ exception.printStackTrace()
  }

  val qq = Source(1 to 100)
    .map { number =>
      val partition = 0
      val value = number.toString
      ProducerMessage.Message(
        new ProducerRecord("topic1", partition, "key", value),
        number)
    }
    .via(Producer.flexiFlow(producerSettings))
    .map {
      case ProducerMessage.Result(metadata, message) =>
        val record = message.record
        s"${metadata.topic}/${metadata.partition} ${metadata.offset}: ${record.value}"

      case ProducerMessage.MultiResult(parts, passThrough) =>
        parts
          .map {
            case MultiResultPart(metadata, record) =>
              s"${metadata.topic}/${metadata.partition} ${metadata.offset}: ${record.value}"
          }
          .mkString(", ")

      case ProducerMessage.PassThroughResult(passThrough) =>
        s"passed through"
    }
    .runWith(Sink.foreach(println(_)))

}
