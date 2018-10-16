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

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.stream.ActorMaterializer
import com.google.inject.{ AbstractModule, Module, Provider }
import com.maogogo.cocoa.common.cluster.SimpleClusterListener
import com.typesafe.config.{ Config, ConfigFactory }
import javax.inject.Inject
import net.codingwell.scalaguice.ScalaModule
import com.maogogo.cocoa.common.inject._

object SysAndConfigModule {

  lazy val SystemName = "MyClusterSystem"

  lazy val SystemPrefix = s"akka.tcp://$SystemName@"

  def defaults: Seq[Module] = {
    Seq(ActorConfigModule(), ActorSystemModule(), ActorMaterializerModule())
  }

  def withConfig(config: Option[Config] = None, cluster: Option[Boolean] = None): Seq[Module] = {
    Seq(
      ActorConfigModule(config),
      ActorSystemModule(config, cluster), ActorMaterializerModule())
  }

}

private object ActorSystemModule {
  def apply(configOpt: Option[Config] = None, clusterOpt: Option[Boolean] = None): AbstractModule with ScalaModule =
    new AbstractModule with ScalaModule {
      override def configure(): Unit = {
        val cfg = configOpt.getOrElse(ConfigFactory.load())
        implicit val system = ActorSystem(name = SysAndConfigModule.SystemName, config = cfg)
        bind[ActorSystem].toInstance(system)

        clusterOpt match {
          case Some(true) ⇒
            bind[Cluster].toInstance(Cluster(system))
            bind[SimpleClusterListener]
          case _ ⇒
        }
      }
    }

}

private object ActorMaterializerModule {

  class ActorMaterializerProvider @Inject() (system: ActorSystem) extends Provider[ActorMaterializer] {
    override def get(): ActorMaterializer = ActorMaterializer()(system)
  }

  def apply(): AbstractModule with ScalaModule =
    new AbstractModule with ScalaModule {
      override def configure(): Unit = {
        bind[ActorMaterializer].toProvider[ActorMaterializerProvider].asEagerSingleton()
      }
    }

}

private object ActorConfigModule {
  def apply(config: Option[Config] = None): AbstractModule with ScalaModule =
    new AbstractModule with ScalaModule {
      override def configure(): Unit = {
        val cfg = config.getOrElse(ConfigFactory.load())
        bind[Config].toInstance(cfg)
      }
    }
}
