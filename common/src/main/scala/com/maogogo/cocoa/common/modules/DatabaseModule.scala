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

package com.maogogo.cocoa.common.modules

import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object DatabaseModule {

  def apply(path: String = "slick-mysql"): AbstractModule with ScalaModule = {
    new AbstractModule with ScalaModule {
      override def configure(): Unit = {
        val databaseConfig = DatabaseConfig.forConfig[JdbcProfile](path)
        bind[SlickSession].toInstance(SlickSession.forConfig(databaseConfig))
      }
    }
  }

}
