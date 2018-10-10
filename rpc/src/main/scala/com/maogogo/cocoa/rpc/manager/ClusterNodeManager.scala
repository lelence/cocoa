package com.maogogo.cocoa.rpc.manager

import akka.actor.Actor
import akka.cluster.Cluster
import com.typesafe.scalalogging.LazyLogging

class ClusterNodeManager(implicit cluster: Cluster) extends Actor with LazyLogging {

  override def receive: Receive = {
    case s: String ⇒ println("")
  }

}
