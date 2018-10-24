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
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.scalatest._

import scala.concurrent.Future

class KafkaProducerSpec extends RpcSpec {

  "emptySeq" in {
    assert(Set.empty.size == 0)
  }

  "sinkTest" in {

    val config = system.settings.config.getConfig("akka.kafka.producer")
    val producerSettings =
      ProducerSettings(config, new StringSerializer, new StringSerializer)
        .withBootstrapServers("localhost:9092")

    val done: Future[Done] =
      Source(1 to 100)
        .map(_.toString)
        .map(value => new ProducerRecord[String, String]("topic1", value))
        .runWith(Producer.plainSink(producerSettings))

    done onComplete {
      case scala.util.Success(value) ⇒ println(value)
      case scala.util.Failure(exception) ⇒ exception.printStackTrace()
    }

  }

}
