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
import com.maogogo.cocoa.common.modules.{ ClusterSystemModule, SysAndConfigModule }
import com.typesafe.config.Config

final object GuiceAkka {

  def apply(): GuiceAkka = new GuiceAkka(Seq.empty)

  def apply(m: Module*): GuiceAkka = new GuiceAkka(m)

  def apply(c: Config): GuiceAkka = new GuiceAkka(Seq.empty).config(c)

  def apply(isCluster: Boolean): GuiceAkka = new GuiceAkka(Seq.empty).cluster()

}

private[common] class GuiceAkka(ms: Seq[Module], config: Option[Config] = None, isCluster: Boolean = false) {

  def injector(): Injector =
    Guice.createInjector((ms :+ SysAndConfigModule(config)): _*)

  def config(c: Config): GuiceAkka = new GuiceAkka(ms, Some(c), isCluster)

  def modules(m: Module*): GuiceAkka = new GuiceAkka(ms ++ m, config, isCluster)

  def cluster(): GuiceAkka = new GuiceAkka(ms :+ ClusterSystemModule(), config, true)

}
