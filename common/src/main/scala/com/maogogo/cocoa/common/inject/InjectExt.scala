package com.maogogo.cocoa.common.inject

import akka.actor.{ ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider }
import com.google.inject.Injector

object InjectExt extends ExtensionId[InjectExtImpl] with ExtensionIdProvider {

  override def createExtension(system: ExtendedActorSystem): InjectExtImpl = {
    new InjectExtImpl
  }

  override def lookup() = InjectExt

  override def get(system: ActorSystem): InjectExtImpl = super.get(system)

}

class InjectExtImpl extends Extension {

  private val injector: ThreadLocal[Injector] = ThreadLocal.withInitial(() ⇒ null)

  def initialize(injector: Injector): Unit = {
    this.injector.set(injector)
  }

  def actorInjector: Injector = injector.get()

}
