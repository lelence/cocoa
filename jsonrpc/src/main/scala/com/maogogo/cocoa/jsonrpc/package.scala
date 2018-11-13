package com.maogogo.cocoa

package object jsonrpc {

  case class JsonRpcRequest(id: Any, method: String, jsonrpc: String, params: Any)


  abstract class AbstractJsonRpcException(
    code: Int,
    message: String,
    id: Option[Any] = None
  ) extends Exception(message)

  case class JsonRpcException(
    code: Int,
    message: String,
    id: Option[Any] = None
  ) extends AbstractJsonRpcException(code, message, id)

  case class JsonRpcResponse(
    id: Option[Any] = None.orNull,
    jsonrpc: String = "2.0",
    result: Any = None,
    error: Option[AbstractJsonRpcException] = None
  )


  //  -32600	Invalid Request无效请求	发送的json不是一个有效的请求对象。
  //  -32601	Method not found找不到方法	该方法不存在或无效
  //  -32602	Invalid params无效的参数	无效的方法参数。
  //  -32603	Internal error内部错误	JSON-RPC内部错误。
  //  -32000 to -32099	Server error服务端错误	预留用于自定义的服务器错误。

  case object JsonRpcParseException extends AbstractJsonRpcException(-32700, "Json parse failed")

  case object JsonRpcInvalidException extends AbstractJsonRpcException(-32600, "Invalid request")

  case object JsonRpcMethodException extends AbstractJsonRpcException(-32601, "Method not found")

  case object JsonRpcParamsException extends AbstractJsonRpcException(-32602, "Parameters invalid")

  case class JsonRpcInternalException(
    message: String = "Internal error",
    id: Option[Any] = None)
    extends AbstractJsonRpcException(-32603, message, id)


}
