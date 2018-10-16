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
