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

package com.maogogo.cocoa.common.cluster

import akka.actor.{ Actor, ActorRef }
import akka.cluster.Cluster
import com.google.inject._
import com.google.inject.name.{ Named, Names }
import com.maogogo.cocoa.common.Constants
import com.maogogo.cocoa.common.actor.ActorBuilder
import net.codingwell.scalaguice.{ ScalaMapBinder, ScalaOptionBinder }

class ClusterActorRefFactory(parentBinder: Binder) {

  private val mBinder = ScalaMapBinder.newMapBinder[String, Option[ActorRef]](
    parentBinder, Names.named(Constants.cluster_actor_map))

  import scala.reflect.runtime.universe._

  def bindActor[T <: Actor : Manifest](
    implicit
    t: TypeTag[T]): Unit = {

    val key = t.tpe.typeSymbol.name.encodedName.toString
    mBinder.addBinding(key).toProvider[ClusterSingletonInstance[T]].asEagerSingleton()

  }

}

@Singleton
class ClusterSingletonInstance[T <: Actor] @Inject()(
  @Named("cluster_actor_builder") builder: ClusterSingletonBuilder,
  provider: Provider[T],
  typeLiteral: TypeLiteral[T])(
  implicit
  cluster: Cluster) extends Provider[Option[ActorRef]] {

  private implicit val system = cluster.system

  val dd = typeLiteral.getRawType.getCanonicalName.split("\\.")
  println("===>>>>" + dd.takeRight(2))

  //  cluster.selfRoles

  override def get(): Option[ActorRef] = None // builder(provider, typeLiteral.getRawType.getSimpleName)
}

