package com.maogogo.cocoa

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.google.inject.{ AbstractModule, Provides, Singleton }
import net.codingwell.scalaguice.ScalaModule

class ServicesModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    implicit val system = ActorSystem("MySystem")
    implicit val mat = ActorMaterializer()

  }

  //  @Provides
  //  @Singleton
  //  def provideActorSystem: ActorSystem = ActorSystem("MySystem")

}
