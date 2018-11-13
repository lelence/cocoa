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
import akka.cluster.Cluster
import akka.cluster.routing.{ ClusterRouterGroup, ClusterRouterGroupSettings }
import akka.cluster.singleton.{ ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings }
import akka.routing.RoundRobinGroup

import scala.reflect.runtime.universe._

package object inject {

  //  def props[T <: Actor : Manifest]: Props = propsWith()((_) ⇒ Unit)
  //
  //  def props[T <: Actor : Manifest](ps: Any*): Props = propsWith(ps: _*)((_) ⇒ Unit)
  //
  //  def propsWith[T <: Actor : Manifest](ps: Any*)(fallback: Props ⇒ Unit)(
  //    implicit
  //    m: Manifest[T]): Props = {
  //    val props = Props(m.runtimeClass, ps: _*)
  //    fallback(props)
  //    props
  //  }
  //
  //  def propsTo[T <: Actor : Manifest, R](ps: Any*)(fallback: Props ⇒ R)(
  //    implicit
  //    m: Manifest[T]): R = {
  //    fallback(Props(m.runtimeClass, ps: _*))
  //  }

  //  def propsTo[T <: Actor : Manifest](named: String)(ps: Any*)(
  //    implicit
  //    system: ActorSystem,
  //    m: Manifest[T]
  //  ): ActorRef = {
  //    propsTo(ps: _*) {
  //      system.actorOf(_, named)
  //    }
  //  }

  //  def propsTo[T <: Actor : Manifest](named: String, role: Option[String] = None)(ps: Any*)(
  //    implicit
  //    cluster: Cluster,
  //    m: Manifest[T]
  //  ): ActorRef = {
  //
  //    propsTo(ps: _*) { props ⇒
  //      cluster.system.actorOf(
  //        ClusterSingletonManager.props(
  //          singletonProps = props,
  //          terminationMessage = PoisonPill,
  //          settings = ClusterSingletonManagerSettings(cluster.system).withRole(role)),
  //        name = named)
  //    }
  //
  //  }

  //  implicit class ActorSystemPropsExtension(props: Props)(implicit system: ActorSystem) {
  //    def to(named: String): ActorRef = system.actorOf(props, named)
  //  }
  //
  //  implicit class ClusterPropsExtension(props: Props)(implicit cluster: Cluster) {
  //
  //    private lazy val system = cluster.system
  //
  //    def to(named: String, role: Option[String] = None): Unit = {
  //
  //      // TODO(Toan) 这里还需要在测试一下
  //      if (role.isEmpty || (role.isDefined && cluster.selfRoles.contains(role.get))) {
  //        system.actorOf(
  //          ClusterSingletonManager.props(
  //            singletonProps = props,
  //            terminationMessage = PoisonPill,
  //            settings = ClusterSingletonManagerSettings(system).withRole(role)),
  //          name = named)
  //      }
  //
  //    }
  //
  //  }

}
