package com.maogogo.cocoa.jsonrpc


object JsonRpcError extends App {

  import org.json4s.NoTypeHints
  import org.json4s.native.Serialization

  implicit val formats = Serialization.formats(NoTypeHints)

  println(Serialization.write(Seq(1, 2, 3)))

}
