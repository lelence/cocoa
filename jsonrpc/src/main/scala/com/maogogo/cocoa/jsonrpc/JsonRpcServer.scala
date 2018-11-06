package com.maogogo.cocoa.jsonrpc

import org.json4s._
import org.json4s.jackson.Serialization
import org.slf4j.LoggerFactory

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try

object JsonRpcServer {
  def apply(settings: JsonRpcSettings): JsonRpcServer = new JsonRpcServer(settings)
}

class JsonRpcServer(settings: JsonRpcSettings) {

  private[jsonrpc] lazy val logger = LoggerFactory.getLogger(getClass)

  private[jsonrpc] implicit val formats = DefaultFormats

  //  -32700	解析错误	服务器接收到无效的JSON；服务器解析JSON文本发生错误。
  //  -32600	无效的请求	发送的JSON不是一个有效的请求。
  //  -32601	方法未找到	方法不存在或不可见。
  //  -36602	无效的参数	无效的方法参数。
  //  -36603	内部错误	JSON-RPC内部错误。
  //  -32000到-32099	服务器端错误	保留给具体实现服务器端错误。
  def handleRequest(json: String)(
    implicit
    ex: ExecutionContext
  ): Future[Option[String]] = {

    val futureValue = try {

      val request = parseJson(json)

      val mmOption = settings.findProvider(request.method)

      require(mmOption.isDefined, s"can not found method: ${request.method} mapping")

      JsonRpcProxy.invoke(request, mmOption.get)

    } catch {
      case e: JsonRpcException ⇒
        logger.error(s"${e.code} @ ${e.message}", e)
        Future(JsonRpcResponse(id = e.id, error = Some(e.copy(id = None))))
      case e: Exception ⇒
        logger.error(s"failed @ ${e.getLocalizedMessage}", e)
        Future(JsonRpcResponse(error = Some(JsonRpcException(code = -36603, message = e.getMessage))))
    }


    futureValue map { resp ⇒
      Try(Serialization.write(resp)).toOption
    }

  }

  private[jsonrpc] def parseJson(json: String): JsonRpcRequest = {

    import org.json4s.jackson.JsonMethods._

    val jValue = parse(json)

    val idOpt = jValue \\ "id" match {
      case JString(x) if x.nonEmpty ⇒ Some(x.toInt)
      case JInt(x) ⇒ Some(x.toInt)
      case _ ⇒ None
    }

    val mthOpt = jValue \\ "method" match {
      case JString(x) if x.nonEmpty ⇒ Some(x.trim)
      case _ ⇒ None
    }

    val rpcOpt = jValue \\ "jsonrpc" match {
      case JString(x) if x == "2.0" ⇒ Some(x)
      case _ ⇒ None
    }

    require(idOpt.isDefined, s"""not found "id" from request json: ${json}""")

    require(mthOpt.isDefined, s"""invalid field "method" from request json: ${json}""")

    require(rpcOpt.isDefined, s"""invalid field "jsonrpc", only "2.0",  from request json: ${json}""")

    JsonRpcRequest(id = idOpt.get, jsonrpc = rpcOpt.get, method = mthOpt.get, params = compact(render(jValue \\ "params")))

  }

}
