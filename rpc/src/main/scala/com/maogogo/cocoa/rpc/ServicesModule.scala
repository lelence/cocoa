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

package com.maogogo.cocoa.rpc

import com.maogogo.cocoa.common.inject._
import akka.actor.{ ActorRef, ActorSystem, Props }
import com.google.inject._
import com.google.inject.name.Named
import com.maogogo.cocoa.rpc.services.{ HelloActor, TestActor }
import net.codingwell.scalaguice.ScalaModule

trait ServicesModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {

  }

  @Provides
  @Singleton
  @Named("haha")
  def aa(implicit system: ActorSystem, @Named("dudu") testActor: ActorRef): ActorRef = {
    Props(classOf[HelloActor], testActor).registerSingleton("dudu")
  }

  @Provides
  @Singleton
  @Named("dudu")
  def bb(implicit system: ActorSystem): ActorRef = {
    Props(classOf[TestActor]).register("dudu")
  }

}

object ServicesModule extends ServicesModule
