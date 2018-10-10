package com.maogogo.cocoa.common.inject

import akka.actor.{ Actor, IndirectActorProducer }
import com.google.inject.name.Names
import com.google.inject.{ Injector, Key }

class GuiceActorProducer(actorName: String)(implicit injector: Injector) extends IndirectActorProducer {

  override def produce(): Actor =
    injector.getBinding(Key.get(classOf[Actor], Names.named(actorName))).getProvider.get()

  override def actorClass: Class[Actor] = classOf[Actor]

}
