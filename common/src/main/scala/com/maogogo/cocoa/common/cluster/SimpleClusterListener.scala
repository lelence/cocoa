package com.maogogo.cocoa.common.cluster

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject

class SimpleClusterListener @Inject()(cluster: Cluster) extends Actor with LazyLogging {

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) ⇒
      logger.info("Member is Up: {}", member.address)
    case UnreachableMember(member) ⇒
      logger.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) ⇒
      logger.info(
        "Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: MemberEvent ⇒ // ignore
  }

}
