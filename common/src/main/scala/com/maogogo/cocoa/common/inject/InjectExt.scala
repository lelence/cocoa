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

package com.maogogo.cocoa.common.inject

import akka.actor.{ ActorRef, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider, Props }
import com.google.inject.Injector

import scala.collection.immutable

object InjectExt extends ExtensionId[InjectExtImpl] with ExtensionIdProvider {

  override def createExtension(system: ExtendedActorSystem): InjectExtImpl = {
    new InjectExtImpl
  }

  override def lookup() = InjectExt

  override def get(system: ActorSystem): InjectExtImpl = super.get(system)

}

class InjectExtImpl extends Extension {

  private val injector: ThreadLocal[Injector] = ThreadLocal.withInitial(() ⇒ null)

  private val actorRefs: ThreadLocal[immutable.Map[String, ActorRef]] = ThreadLocal.withInitial(() ⇒ immutable.Map.empty)

  private[common] def initialize(injector: Injector): Unit = {
    this.injector.set(injector)
  }

  def actorInjector: Injector = injector.get()

  def createActorRef(prop: Props, named: String)(implicit system: ActorSystem): ActorRef = {
    actorRefs.get().get(named) match {
      case Some(ref) ⇒ ref
      case _ ⇒
        val ref = system.actorOf(prop, named)
        actorRefs.set(actorRefs.get() + (named → ref))
        ref
    }
  }

  def registerSingleCluster(prop: Props, named: String, role: String)(implicit system: ActorSystem): Unit = {
    // sys.actorOf(
    //      ClusterSingletonManager.props(
    //        singletonProps = Props(classOf[Consumer], queue, testActor),
    //        terminationMessage = PoisonPill,
    //        settings = ClusterSingletonManagerSettings(system).withRole("worker")),
    //      name = "consumer")
  }

}
