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
//import com.corundumstudio.socketio.{ AckRequest, SocketIOClient }
//
//case class SocketIOEvent[T](client: SocketIOClient, t: T, ackSender: AckRequest)
//
//case class SocketInMessage[T](event: String, t: T, headers: Map[String, String])
//
//trait SocketOutMessage
//
//case class BroadcastMessage[T](event: String, t: T) extends SocketOutMessage
//
//case class EventMessage[T](event: String, t: T) extends SocketOutMessage
//
//case class ReplayMessage[T](t: T) extends SocketOutMessage
//
//case object NoneMessage extends SocketOutMessage
