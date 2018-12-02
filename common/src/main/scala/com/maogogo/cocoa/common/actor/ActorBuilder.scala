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

package com.maogogo.cocoa.common.actor

import akka.actor.{ Actor, ActorRef, ActorSystem, PoisonPill, Props }
import akka.cluster.singleton.{ ClusterSingletonManager, ClusterSingletonManagerSettings }
import com.google.inject._

trait ActorBuilder {
  def apply(provider: Provider[_ <: Actor], named: String)(
    implicit
    system: ActorSystem): ActorRef
}

//
//class ActorBuilderImpl extends ActorBuilder {
//
//  def apply(provider: Provider[_ <: Actor], named: String)(
//    implicit
//    system: ActorSystem): ActorRef = {
//
//    // Props.empty
//    system.actorOf(Props(provider.get()), named)
//  }
//
//}

//class ClusterActorBuilderImpl extends ActorBuilder {
//
//  def apply(provider: Provider[_ <: Actor], named: String)(
//    implicit
//    system: ActorSystem): ActorRef = {
//
//    system.actorOf(
//      ClusterSingletonManager.props(
//        singletonProps = Props(provider.get),
//        terminationMessage = PoisonPill,
//        settings = ClusterSingletonManagerSettings(system)),
//      named)
//  }
//
//}

