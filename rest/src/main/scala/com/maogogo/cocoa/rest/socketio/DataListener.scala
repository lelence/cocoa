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

///*
// * Copyright 2018 Maogogo Workshop
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.maogogo.cocoa.rest.socketio
//
//import akka.actor.ActorRef
//import akka.util.Timeout
//import akka.pattern.ask
//import com.corundumstudio.socketio.{ AckRequest, SocketIOClient }
//import com.fasterxml.jackson.databind.ObjectMapper
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//class DataListener[T] extends com.corundumstudio.socketio.listener.DataListener[T] {
//
//  override def onData(client: SocketIOClient, data: T, ackSender: AckRequest): Unit = {
//
//  }
//}
//
//private[socketio] class ActorDataListener[I, O](event: String, replyTo: ActorRef)(
//  implicit
//  serializr: String ⇒ I,
//  deserializr: O ⇒ java.util.Map[String, Any])
//  extends com.corundumstudio.socketio.listener.DataListener[Any] {
//
//  implicit val timeout = Timeout(3 seconds)
//
//  override def onData(client: SocketIOClient, data: Any, ackSender: AckRequest): Unit = {
//
//    import scala.collection.JavaConverters._
//
//    val headersJava = client.getHandshakeData.getHttpHeaders
//    val headersMap = headersJava.asScala.foldLeft(Map.empty[String, String]) { (all, x) ⇒
//      all + (x.getKey → x.getValue)
//    }
//
//    val json = new ObjectMapper().writeValueAsString(data)
//
//    val inMessage = SocketInMessage[I](event, serializr(json), headersMap)
//
//    val messageFuture = (replyTo ? inMessage).mapTo[SocketOutMessage]
//
//    val message = Await.result(messageFuture, Duration.Inf)
//
//    // TODO(Toan) message
//    message match {
//      case b: BroadcastMessage[O] ⇒
//        val map = deserializr(b.t)
//        client.getNamespace.getBroadcastOperations.sendEvent(b.event, map)
//      case e: EventMessage[O] ⇒
//        client.sendEvent(e.event, deserializr(e.t))
//      case r: ReplayMessage[O] ⇒
//        ackSender.sendAckData(deserializr(r.t))
//      case _ ⇒ // logger.info("no message")
//    }
//  }
//
//}
//
