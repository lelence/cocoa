package com.maogogo.cocoa.common.modules

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.stream.ActorMaterializer
import com.google.inject.{ AbstractModule, Module, Provider }
import com.maogogo.cocoa.common.cluster.SimpleClusterListener
import com.typesafe.config.{ Config, ConfigFactory }
import javax.inject.Inject
import net.codingwell.scalaguice.ScalaModule
import com.maogogo.cocoa.common.inject._

object SysAndConfigModule {

  lazy val SystemName = "MyClusterSystem"

  lazy val SystemPrefix = s"akka.tcp://$SystemName@"

  def defaults: Seq[Module] = {
    Seq(ActorConfigModule(), ActorSystemModule(), ActorMaterializerModule())
  }

  def withConfig(config: Option[Config] = None, cluster: Option[Boolean] = None): Seq[Module] = {
    Seq(
      ActorConfigModule(config),
      ActorSystemModule(config, cluster), ActorMaterializerModule())
  }

}

private object ActorSystemModule {
  def apply(configOpt: Option[Config] = None, clusterOpt: Option[Boolean] = None): InjectorModule =
    new AbstractModule with ScalaModule {
      override def configure(): Unit = {
        val cfg = configOpt.getOrElse(ConfigFactory.load())
        implicit val system = ActorSystem(name = SysAndConfigModule.SystemName, config = cfg)
        bind[ActorSystem].toInstance(system)

        clusterOpt match {
          case Some(true) ⇒
            bind[Cluster].toInstance(Cluster(system))
            bind[SimpleClusterListener]
          case _ ⇒
        }
      }
    }

}

private object ActorMaterializerModule {

  class ActorMaterializerProvider @Inject()(system: ActorSystem) extends Provider[ActorMaterializer] {
    override def get(): ActorMaterializer = ActorMaterializer()(system)
  }

  def apply(): InjectorModule =
    new AbstractModule with ScalaModule {
      override def configure(): Unit = {
        bind[ActorMaterializer].toProvider[ActorMaterializerProvider].asEagerSingleton()
      }
    }

}

private object ActorConfigModule {
  def apply(config: Option[Config] = None): InjectorModule =
    new AbstractModule with ScalaModule {
      override def configure(): Unit = {
        val cfg = config.getOrElse(ConfigFactory.load())
        bind[Config].toInstance(cfg)
      }
    }
}
