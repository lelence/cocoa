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
import com.google.inject.{ AbstractModule, Provider }
import com.maogogo.cocoa.common.cluster.SimpleClusterListener
import com.typesafe.config.{ Config, ConfigFactory }
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject
import net.codingwell.scalaguice.ScalaModule

private[common] object SysAndConfigModule extends LazyLogging {

  lazy val systemName = "MyClusterSystem"

  def apply(config: Option[Config] = None, withCluster: Option[Boolean] = None): AbstractModule with ScalaModule =
    new AbstractModule with ScalaModule {
      override def configure(): Unit = {

        val system = ActorSystem(systemName, config.getOrElse(ConfigFactory.load()))
        bind[ActorSystem].toInstance(system)
        bind[Config].toInstance(system.settings.config)

        withCluster match {
          case Some(true) ⇒
            bind[Cluster].toInstance(Cluster(system))
            bind[SimpleClusterListener]
          case _ ⇒
        }

        bind[ActorMaterializer].toProvider[ActorMaterializerProvider].asEagerSingleton()
      }
    }

  class ActorMaterializerProvider @Inject() (system: ActorSystem) extends Provider[ActorMaterializer] {
    override def get(): ActorMaterializer = ActorMaterializer()(system)
  }

}
