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

package com.maogogo.cocoa.rest

import akka.actor.{ ActorRef, ActorSystem }
import com.google.inject.name.Named
import com.google.inject.{ AbstractModule, Provides, Singleton }
import com.maogogo.cocoa.common.inject._
import com.maogogo.cocoa.rest.endpoints.RootEndpoint
import com.maogogo.cocoa.rest.http.HttpServer
import net.codingwell.scalaguice.ScalaModule

trait ServicesModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[RootEndpoint]
    bind[HttpServer]
    // bind[EventTest].to[EventTestImpl]

  }

  @Provides
  @Singleton
  @Named("uhaha")
  def aa(implicit system: ActorSystem): ActorRef = {
    //
    // clusterProxy("/user/haha", "uhaha")
    ???
  }

}

object ServicesModule extends ServicesModule
