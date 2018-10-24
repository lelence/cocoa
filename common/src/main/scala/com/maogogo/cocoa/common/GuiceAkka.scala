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

import com.google.inject._
import com.maogogo.cocoa.common.modules.SysAndConfigModule
import com.typesafe.config.{ Config, ConfigFactory }

final object GuiceAkka {

  def apply(): Injector = apply(ConfigFactory.load())

  def apply(config: Config): Injector = apply(config, None, Seq.empty: _*)

  def apply(m: Module*): Injector = apply(ConfigFactory.load(), None, m: _*)

  def apply(c: Config, m: Module*): Injector = apply(c, None, m: _*)

  def apply(c: Config, cluster: Option[Boolean], m: Module*): Injector =
    Guice.createInjector((m :+ SysAndConfigModule(Some(c), cluster)): _*)

  def withCluster(config: Config): Injector = withCluster(config, Seq.empty: _*)

  def withCluster(m: Module*): Injector = withCluster(ConfigFactory.load(), m: _*)

  def withCluster(config: Config, m: Module*): Injector = apply(config, Some(true), m: _*)

}
