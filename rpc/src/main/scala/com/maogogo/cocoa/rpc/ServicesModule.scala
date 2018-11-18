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

import akka.actor.{ Actor, ActorRef, ActorSystem, PoisonPill, Props }
import akka.cluster.Cluster
import akka.cluster.singleton.{ ClusterSingletonManager, ClusterSingletonManagerSettings }
import com.google.inject._
import com.google.inject.name.{ Named, Names }
import com.maogogo.cocoa.common.cluster.ClusterActorRefFactory
import com.maogogo.cocoa.rpc.node.NodeHttpServer
//import com.maogogo.cocoa.common.inject.{ ActorBuilder }
import com.maogogo.cocoa.rpc.actors.HelloActor
import net.codingwell.scalaguice.{ ScalaMapBinder, ScalaModule }

class ServicesModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {

    val factory = new ClusterActorRefFactory(binder)
    factory.bindActor[HelloActor] //("HelloActorSS")

    // bind[NodeHttpServer]

    //    val mBinder = ScalaMapBinder.newMapBinder[String, ActorRef](binder, Names.named("uu"))
    //    mBinder.addBinding("HelloActor").toProvider[ServicesModule.CI[HelloActor]]

  }

}

//object ServicesModule {
//  class AI[T <: Actor] @Inject() (
//    @Named("actor_builder") builder: ActorBuilder, provider: Provider[T], typeLiteral: TypeLiteral[T])(
//    implicit
//    system: ActorSystem) extends Provider[ActorRef] {
//
//    override def get(): ActorRef = builder(provider, typeLiteral.getRawType.getSimpleName)
//  }
//
//  @Singleton
//  class CI[T <: Actor] @Inject() (
//    @Named("cluster_actor_builder") builder: ActorBuilder, provider: Provider[T], typeLiteral: TypeLiteral[T])(
//    implicit
//    cluster: Cluster) extends Provider[ActorRef] {
//
//    private implicit val system = cluster.system
//
//    override def get(): ActorRef = builder(provider, typeLiteral.getRawType.getSimpleName)
//  }
//}
