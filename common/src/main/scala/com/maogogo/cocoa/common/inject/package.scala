package com.maogogo.cocoa.common

import akka.actor.{ Actor, ActorRef, ActorSystem }
import akka.cluster.Cluster
import akka.stream.Materializer
import com.google.inject.internal.BindingBuilder
import com.google.inject.{ AbstractModule, Injector, Key }
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule

package object inject {

  import Internals._

  type InjectorModule = AbstractModule with ScalaModule

  type InjectorProvider = () => Injector

  def injectActor[T <: Actor : Manifest](implicit system: ActorSystem): ActorInjectionBuilder[T] = {
    new ActorInjectionBuilderImpl[T]
  }

  implicit def actorSystem2injectorProvider(implicit sys: ActorSystem): InjectorProvider = () => InjectExt(sys).actorInjector

  def injector[T](implicit ip: InjectorProvider): InjectionBuilder[T] = {
    new InjectionBuilderImpl[T]
  }

  implicit class ActorInjector(injector: Injector) {

    def actorSystem: ActorSystem = injector.getInstance(classOf[ActorSystem])

  }

  //  implicit class aa[T: Manifest](builder: BindingBuilder[T]) {
  //
  //    def deployCluster(implicit system: ActorSystem)= {
  //
  //      // InjectExt(system).actorInjector
  //      builder
  //    }
  //
  //
  //  }

}
