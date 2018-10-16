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

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import akka.cluster.Cluster
import akka.stream.Materializer
import com.google.inject.internal.BindingBuilder
import com.google.inject.{ AbstractModule, Injector, Key }
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule

package object inject {

  //  import Internals._

  import net.codingwell.scalaguice.InjectorExtensions._

  type InjectorProvider = () => Injector

  implicit def system2Provider(sys: ActorSystem): InjectorProvider = () ⇒ InjectExt(sys).actorInjector

  def injector[T: Manifest](implicit ip: InjectorProvider): T = ip().instance[T]

  def injectorRef(named: String)(implicit sys: ActorSystem): ActorRef = {
    val ip = system2Provider(sys)
    val prop = ip().instance[Props](Names.named(named))
    InjectExt(sys).createActorRef(prop, named)
  }

  def registerSingleton(named: String)(implicit sys: ActorSystem): Unit = {
    val ip = system2Provider(sys)
    val prop = ip().instance[Props](Names.named(named))

    //    sys.actorOf(
    //      ClusterSingletonManager.props(
    //        singletonProps = Props(classOf[Consumer], queue, testActor),
    //        terminationMessage = End,
    //        settings = ClusterSingletonManagerSettings(system).withRole("worker")),
    //      name = "consumer")

  }

}
