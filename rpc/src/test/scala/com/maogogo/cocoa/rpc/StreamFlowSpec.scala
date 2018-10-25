package com.maogogo.cocoa.rpc

import akka.NotUsed
import akka.actor.ActorRef
import akka.stream.{ FlowShape, SinkShape }
import akka.stream.scaladsl.{ Broadcast, Flow, GraphDSL, Sink, Source, Zip }

import scala.concurrent.Future

class StreamFlowSpec extends RpcSpec {

  "test1" in {

    // val flow: Flow[String, String, ActorRef] = _

    val sink = Sink.foreach[String](println)

    //    val source = Source.

    import GraphDSL.Implicits._

    val foldFlow: Flow[Int, Int, Future[Int]] =
      Flow.fromGraph(GraphDSL.create(Sink.fold[Int, Int](0)(_ + _)) { implicit builder ⇒
        fold ⇒
          // val dd: SinkShape[Int] = fold
          FlowShape(fold.in, builder.materializedValue.mapAsync(4)(identity).outlet)
      })


    // Flow.fromSinkAndSource()

    val pairUpWithToString: Flow[Int, (Int, String), NotUsed] =
      Flow.fromGraph(GraphDSL.create() { implicit b ⇒


        // prepare graph elements
        val broadcast = b.add(Broadcast[Int](2))
        val zip = b.add(Zip[Int, String]())

        // connect the graph
        broadcast.out(0).map(identity) ~> zip.in0
        broadcast.out(1).map(_.toString) ~> zip.in1

        // expose ports
        FlowShape(broadcast.in, zip.out)
      })

    // val dd = pairUpWithToString.runWith(Source(List(1)), Sink.head)
  }

}
