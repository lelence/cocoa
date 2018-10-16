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
