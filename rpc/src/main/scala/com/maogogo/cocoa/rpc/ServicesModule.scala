package com.maogogo.cocoa.rpc

import com.google.inject.AbstractModule
import com.maogogo.cocoa.rpc.services.HelloActor
import net.codingwell.scalaguice.ScalaModule

trait ServicesModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[HelloActor]
  }

}

object ServicesModule extends ServicesModule
