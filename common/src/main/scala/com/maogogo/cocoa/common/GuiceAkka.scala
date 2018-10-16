package com.maogogo.cocoa.common

import akka.actor.ActorSystem
import com.google.inject.{ Binder, Guice, Injector, Module }
import com.maogogo.cocoa.common.inject.InjectExt
import com.maogogo.cocoa.common.modules.SysAndConfigModule
import com.typesafe.config.Config

final object GuiceAkka {

  def apply(): Injector =
    this(SysAndConfigModule.defaults: _*)

  def apply(cluster: Option[Boolean], modules: Module*): Injector =
    this(SysAndConfigModule.withConfig(None, cluster) ++ modules: _*)

  def apply(config: Config, cluster: Option[Boolean], modules: Module*): Injector =
    this(SysAndConfigModule.withConfig(Some(config), cluster) ++ modules: _*)

  def apply(modules: Module*): Injector = {
    val injector = Guice.createInjector(modules: _*)
    val system = injector.getInstance(classOf[ActorSystem])
    // init actor injector
    InjectExt(system).initialize(injector)
    injector
  }

  def withSystem(modules: Module*): Injector = {
    apply(SysAndConfigModule.defaults ++ modules: _*)
  }

}
