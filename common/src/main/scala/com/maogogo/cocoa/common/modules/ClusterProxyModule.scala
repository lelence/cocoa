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

package com.maogogo.cocoa.common.modules

import akka.actor.{ ActorRef, ActorSystem }
import akka.cluster.singleton.{ ClusterSingletonProxy, ClusterSingletonProxySettings }
import com.google.inject.{ AbstractModule, Binder, Provider }
import com.maogogo.cocoa.common.cluster.{ ProxyActor, ProxyActorProvider }
import com.typesafe.config.Config
import javax.inject.Inject
import net.codingwell.scalaguice.ScalaModule

object ClusterProxyModule {

  def apply(): AbstractModule with ScalaModule = {
    new AbstractModule with ScalaModule with AssistedInjectFactoryScalaModule[Binder] {
      override def configure(): Unit = {

        bind[Map[String, ActorRef]]
          .annotatedWithName("cluster_proxy_routees")
          .toProvider[ProxyRouteesProvider]
          .asEagerSingleton()

        bindFactory[ProxyActorProvider, ProxyActor]()
      }
    }
  }

  class ProxyRouteesProvider @Inject() (
    config: Config,
    system: ActorSystem) extends Provider[Map[String, ActorRef]] {

    import scala.collection.JavaConverters._

    override def get(): Map[String, ActorRef] = {

      def proxy(named: String, system: ActorSystem): ActorRef = {
        system.actorOf(
          ClusterSingletonProxy.props(
            singletonManagerPath = s"/user/${named}",
            settings = ClusterSingletonProxySettings(system)),
          name = s"proxy_${named}")
      }

      config.getStringList("akka.cluster.routees").asScala.map { path ⇒
        path → proxy(path, system)
      } toMap
    }
  }

}

