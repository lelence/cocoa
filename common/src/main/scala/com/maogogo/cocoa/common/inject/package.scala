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

package com.maogogo.cocoa.common

import akka.actor.{ Actor, ActorRef, ActorSystem, PoisonPill, Props }
import akka.cluster.client.ClusterClientReceptionist
import akka.cluster.singleton.{ ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings }

import scala.reflect.runtime.universe._

package object inject {

  def props[T <: Actor : Manifest](ps: Any*)(
    implicit
    m: Manifest[T]
  ): Props = Props(classOf[T], ps: _*)

  def actorRef[T <: Actor : Manifest](named: String, ps: Any*)(
    implicit
    system: ActorSystem,
    m: Manifest[T]): ActorRef =
    system.actorOf(props[T](ps: _*), named)

  def clusterSingleton[T <: Actor : Manifest](named: String, ps: Any*)(
    implicit
    system: ActorSystem,
    m: Manifest[T]): ActorRef = clusterSingleton(named, None, ps: _*)

  def clusterSingleton[T <: Actor : Manifest](named: String, role: Option[String], ps: Any*)(
    implicit
    system: ActorSystem,
    m: Manifest[T]): ActorRef = {
    system.actorOf(
      ClusterSingletonManager.props(
        singletonProps = props[T](ps),
        terminationMessage = PoisonPill,
        settings = ClusterSingletonManagerSettings(system).withRole(role)),
      name = named)
  }


  def clusterProxy(path: String, named: String, role: Option[String] = None)(
    implicit
    system: ActorSystem): ActorRef = {
    system.actorOf(
      ClusterSingletonProxy.props(
        singletonManagerPath = path,
        settings = ClusterSingletonProxySettings(system).withRole(role)),
      name = named)
  }

  implicit class PropsExtension(props: Props)(implicit system: ActorSystem) {

    def registerActor[T <: Actor : Manifest](named: String): ActorRef = system.actorOf(Props[T], named)

    def register(named: String): ActorRef = system.actorOf(props, named)

    def registerActor2Cluster[T <: Actor : Manifest](named: String): ActorRef = {
      val actorRef = registerActor[T](named)
      ClusterClientReceptionist(system).registerService(actorRef)
      actorRef
    }

    def registerCluster(named: String): ActorRef = {
      val actorRef = register(named)
      ClusterClientReceptionist(system).registerService(actorRef)
      actorRef
    }

    def registerSingleton(named: String, role: Option[String] = None): ActorRef = {
      system.actorOf(
        ClusterSingletonManager.props(
          singletonProps = props,
          terminationMessage = PoisonPill,
          settings = ClusterSingletonManagerSettings(system).withRole(role)),
        name = named)
    }

  }

}
