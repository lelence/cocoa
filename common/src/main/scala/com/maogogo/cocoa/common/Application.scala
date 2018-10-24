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

import com.maogogo.cocoa.common.modules.SysAndConfigModule

trait Application extends App {

  lazy val systemPrefix = s"akka.tcp://${SysAndConfigModule.systemName}@"

  /**
   * sbt "project/run -p=2552"
   * sbt "project/run --port 2552"
   *
   * cocoa 1.0
   * Usage :
   *
   * -p, --port Int server port
   * -s, --seeds Seq[String] cluster seeds
   * -r, --role Seq[String] cluster role
   */
  new scopt.OptionParser[CommandSettings]("scopt") {
    head("cocoa", "1.0")

    opt[Int]('p', "port").action((x, c) => {
      c.copy(port = x)
    }).text("server port")

    opt[Seq[String]]('s', "seeds").action((x, c) => {
      c.copy(seeds = x)
    }).text("cluster seeds")

    opt[Seq[String]]('r', "role").action((x, c) => {
      c.copy(role = x)
    }).text("cluster role")

  }.parse(args, CommandSettings()) match {
    case Some(s) ⇒
      parser(s); println(logo)
    case _ ⇒
  }

  val parser: CommandSettings ⇒ Unit

  val logo: String

}

case class CommandSettings(
  port: Int = 2552,
  seeds: Seq[String] = Seq.empty,
  role: Seq[String] = Seq.empty)
