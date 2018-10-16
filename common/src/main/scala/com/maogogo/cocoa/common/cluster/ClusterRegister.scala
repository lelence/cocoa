package com.maogogo.cocoa.common.cluster

import com.maogogo.cocoa.common.inject._
import akka.actor.{ Actor, ActorSystem, PoisonPill, Props }
import akka.cluster.singleton.{ ClusterSingletonManager, ClusterSingletonManagerSettings }
import com.maogogo.cocoa.common.inject.InjectExt

final object ClusterRegister {

  //  def registerSingleton[T <: Actor: Manifest](anno: String)(implicit system: ActorSystem) = {
  //    val actor = injector[T] required
  //
  //    system.actorOf(
  //      ClusterSingletonManager.props(
  //        singletonProps = Props(actor),
  //        terminationMessage = PoisonPill,
  //        settings = ClusterSingletonManagerSettings(system).withRole("worker")),
  //      name = "consumer")
  //
  //  }

}
