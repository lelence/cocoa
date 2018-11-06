package com.maogogo.cocoa

package object jsonrpc {

  case class JsonRpcRequest(id: Int, method: String, jsonrpc: String, params: String)

  case class JsonRpcException(code: Int, message: String, id: Option[Int] = None) extends Exception(message)

  case class JsonRpcResponse(id: Option[Int] = None.orNull, jsonrpc: String = "2.0", result: Any = None, error: Option[JsonRpcException] = None)

}
