package com.maogogo.cocoa.rest

import com.maogogo.cocoa.rest.endpoints.RootEndpoint
import net.codingwell.scalaguice.ScalaModule

trait ServicesModule extends ScalaModule {

  override def configure(): Unit = {
    bind[RootEndpoint]
    bind[HttpServer]
  }

}

object ServicesModule extends ServicesModule
