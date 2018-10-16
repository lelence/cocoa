package com.maogogo.cocoa.rpc.services

import akka.actor.Actor

class HelloActor(name: String) extends Actor {
  override def receive: Receive = {
    case s: String ⇒
      println("ss ==>>>" + s)
      sender() ! "hahahha"
  }
}
