package com.maogogo.cocoa.jsonrpc

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.json4s.jackson.JsonMethods._
import org.json4s.{ JArray, JObject, JValue }

import scala.concurrent.{ ExecutionContext, Future }
import scala.reflect.runtime.universe._

private[jsonrpc] object JsonRpcProxy {

  lazy val m = runtimeMirror(getClass.getClassLoader)

  lazy val objectMapper = mapper.registerModule(DefaultScalaModule)

  def invoke(request: JsonRpcRequest, methodMirror: MethodMirror)(
    implicit
    ex: ExecutionContext
  ): Future[JsonRpcResponse] = {

    val methodName = methodMirror.symbol.name.encodedName.toString

    val paramTpes = methodMirror.symbol.paramLists.flatMap(_.map(_.typeSignature))

    val responseAny = paramTpes match {
      case Seq() ⇒ methodMirror()
      case Seq(head) ⇒

        // params 已经 经过转换了
        request.params match {
          case arr: JArray ⇒

            if (head <:< typeOf[Iterable[_]]) {
              methodMirror(convertString2Parameter(arr, head))
            } else {
              arr.children match {
                case (a0: JValue) :: Nil ⇒ methodMirror(convertString2Parameter(a0, head))
                case _ ⇒ throw JsonRpcInternalException(s"params not match Array", id = Some(request.id))
              }
            }

          case obj: JObject ⇒ methodMirror(convertString2Parameter(obj, head))
          case _ ⇒
            throw JsonRpcInternalException(s"params not match", id = Some(request.id))
        }

      case _ ⇒ throw JsonRpcInternalException(s"method:${methodName} has multiple parameter", id = Some(request.id))
    }

    (responseAny match {
      case f: Future[_] ⇒ f.map(resp ⇒ JsonRpcResponse(id = Some(request.id), result = resp))
      case x ⇒ Future.successful(JsonRpcResponse(id = Some(request.id), result = x))
    })

  }

  def convertString2Parameter(jValue: JValue, tpe: Type) = {
    objectMapper.readValue(compact(render(jValue)), m.runtimeClass(tpe))
  }

}
